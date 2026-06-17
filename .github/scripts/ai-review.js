async function run({ github, context }) {
  const apiKey = process.env.GEMINI_API_KEY;

  if (!apiKey) {
    console.warn("⚠️ Không tìm thấy GEMINI_API_KEY.");
    return;
  }

  const prNumber = context.payload.pull_request.number;

  const files = await github.paginate(github.rest.pulls.listFiles, {
    owner: context.repo.owner,
    repo: context.repo.repo,
    pull_number: prNumber,
  });

  console.log(`📂 ${files.length} file thay đổi`);

  for (const file of files) {
    try {
      if (!file.patch) {
        console.log(`⏭️ Skip ${file.filename} (không có patch)`);
        continue;
      }

      console.log(`🔍 Reviewing ${file.filename}`);

      const prompt = `
Bạn là Principal Java Architect.

Review file sau:

File:
${file.filename}

Patch:

\`\`\`diff
${file.patch}
\`\`\`

Chỉ tìm:

- Bug
- NullPointerException
- Security issue
- Hibernate/JPA issue
- SQL issue
- Performance issue

KHÔNG nhận xét coding style.

Trả về JSON hợp lệ:

[
  {
    "codeSnippet": "đoạn code lỗi",
    "severity": "LOW",
    "comment": "giải thích lỗi"
  }
]

Nếu không có lỗi:

[]
`;

      const response = await fetch(
        `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            contents: [
              {
                parts: [
                  {
                    text: prompt,
                  },
                ],
              },
            ],
          }),
        },
      );

      if (!response.ok) {
        throw new Error(`Gemini API Error: ${response.status}`);
      }

      const data = await response.json();

      let reviewText = data?.candidates?.[0]?.content?.parts?.[0]?.text ?? "[]";

      reviewText = reviewText
        .replace(/```json/g, "")
        .replace(/```/g, "")
        .trim();

      let reviews;

      try {
        reviews = JSON.parse(reviewText);
      } catch (e) {
        console.log(`⚠️ JSON không hợp lệ từ Gemini: ${file.filename}`);
        continue;
      }

      if (!Array.isArray(reviews) || reviews.length === 0) {
        console.log(`✅ LGTM ${file.filename}`);
        continue;
      }

      const patchLines = file.patch.split("\n");

      for (const review of reviews) {
        try {
          const snippet = review.codeSnippet?.trim();

          if (!snippet) continue;

          const patchIndex = patchLines.findIndex(
            (line) => line.includes(snippet) && line.startsWith("+"),
          );

          if (patchIndex === -1) {
            console.log(`⚠️ Không tìm thấy snippet trong patch: ${snippet}`);
            continue;
          }

          let currentLine = 0;

          for (let i = 0; i <= patchIndex; i++) {
            const line = patchLines[i];

            const hunkMatch = line.match(/^@@ -\d+(?:,\d+)? \+(\d+)/);

            if (hunkMatch) {
              currentLine = parseInt(hunkMatch[1], 10) - 1;
              continue;
            }

            if (line.startsWith("+") || line.startsWith(" ")) {
              currentLine++;
            }
          }

          await github.rest.pulls.createReviewComment({
            owner: context.repo.owner,
            repo: context.repo.repo,
            pull_number: prNumber,

            commit_id: context.payload.pull_request.head.sha,

            path: file.filename,

            line: currentLine,

            side: "RIGHT",

            body: [
              `🤖 **AI Review (${review.severity})**`,
              "",
              review.comment,
            ].join("\n"),
          });

          console.log(`💬 Commented ${file.filename}:${currentLine}`);
        } catch (commentError) {
          console.error(
            `❌ Không thể comment line cho ${file.filename}`,
            commentError,
          );
        }
      }

      console.log(`✅ Review hoàn tất ${file.filename}`);
    } catch (error) {
      console.error(`❌ Review lỗi file ${file.filename}`, error);
    }
  }

  console.log("🎉 Hoàn tất AI Review.");
}

module.exports = run;
