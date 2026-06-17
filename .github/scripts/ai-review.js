const fs = require("fs");

async function run({ github, context }) {
  const apiKey = process.env.GEMINI_API_KEY;

  if (!apiKey) {
    console.warn(
      "⚠️ Không tìm thấy GEMINI_API_KEY. Vui lòng cấu hình GitHub Secret."
    );
    return;
  }

  const diffPath = "diff.txt";

  if (!fs.existsSync(diffPath)) {
    console.error("❌ Không tìm thấy file diff.txt");
    return;
  }

  const rawDiff = fs.readFileSync(diffPath, "utf8").trim();

  if (!rawDiff) {
    console.log("ℹ️ Không có thay đổi nào để review.");
    return;
  }

  /**
   * Tách git diff theo từng file
   */
  function splitDiffByFile(diff) {
    const files = [];

    const parts = diff.split(/^diff --git /gm);

    for (const part of parts) {
      if (!part.trim()) continue;

      const lines = part.split("\n");

      const match = lines[0]?.match(/a\/(.+?) b\/(.+)/);

      const fileName = match?.[2] || "unknown-file";

      files.push({
        fileName,
        diff: "diff --git " + part
      });
    }

    return files;
  }

  const changedFiles = splitDiffByFile(rawDiff);

  console.log(
    `📂 Tìm thấy ${changedFiles.length} file thay đổi cần review`
  );

  const MAX_FILE_DIFF_LENGTH = 10000;

  try {
    for (const file of changedFiles) {
      try {
        console.log(`🔍 Reviewing: ${file.fileName}`);

        if (file.diff.length > MAX_FILE_DIFF_LENGTH) {
          console.log(
            `⏭️ Skip ${file.fileName} vì diff quá lớn (${file.diff.length} chars)`
          );

          await github.rest.issues.createReviewComment({
            owner: context.repo.owner,
            repo: context.repo.repo,
            issue_number: context.issue.number,
            body: `## 🤖 AI Review - \`${file.fileName}\`

⚠️ File quá lớn nên AI bỏ qua review tự động.

Kích thước diff: ${file.diff.length} ký tự.
`
          });

          continue;
        }

        const prompt = `
Bạn là Senior Software Engineer và AI Code Reviewer.

Hãy review DUY NHẤT file dưới đây.

Tên file:
${file.fileName}

Git Diff:

\`\`\`diff
${file.diff}
\`\`\`

Yêu cầu:

1. Chỉ review file này.
2. Tìm bug tiềm ẩn.
3. Kiểm tra logic nghiệp vụ.
4. Kiểm tra JPA/Hibernate nếu có.
5. Kiểm tra Security.
6. Kiểm tra Performance.
7. Kiểm tra Clean Code.

Quy tắc trả lời:

- Viết bằng tiếng Việt.
- Ngắn gọn.
- Tối đa 10 ý.
- Nếu không có vấn đề gì đáng chú ý:
  trả về đúng duy nhất:

LGTM
`;

        const response = await fetch(
          `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify({
              contents: [
                {
                  parts: [
                    {
                      text: prompt
                    }
                  ]
                }
              ]
            })
          }
        );

        if (!response.ok) {
          throw new Error(
            `Gemini API Error: ${response.status} ${response.statusText}`
          );
        }

        const data = await response.json();

        const reviewText =
          data?.candidates?.[0]?.content?.parts?.[0]?.text ||
          "Không nhận được phản hồi từ Gemini.";

        let body = `## 🤖 AI Review - \`${file.fileName}\`\n\n`;

        if (reviewText.trim() === "LGTM") {
          body += "✅ LGTM - Không phát hiện vấn đề đáng chú ý.";
        } else {
          body += reviewText;
        }

        body +=
          "\n\n---\n*Review tự động bởi Gemini 2.5 Flash*";

        // GitHub comment giới hạn 65536 ký tự
        if (body.length > 60000) {
          body = body.substring(0, 60000);
          body += "\n\n...(review bị cắt ngắn)...";
        }

        await github.rest.issues.createComment({
          owner: context.repo.owner,
          repo: context.repo.repo,
          issue_number: context.issue.number,
          body
        });

        console.log(`✅ Đã review ${file.fileName}`);
      } catch (fileError) {
        console.error(
          `❌ Lỗi khi review file ${file.fileName}`,
          fileError
        );
      }
    }

    console.log(
      "🎉 Hoàn tất AI Review cho tất cả file thay đổi."
    );
  } catch (error) {
    console.error("❌ AI Review thất bại:", error);
  }
}

module.exports = run;