const fs = require("fs");

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
${file.patch}

Chỉ tìm:

* Bug
* NPE
* Security
* Hibernate/JPA issue
* SQL issue
* Performance issue

KHÔNG nhận xét coding style.

Trả về JSON:

[
{
"codeSnippet": "<đoạn code lỗi>",
"severity": "LOW|MEDIUM|HIGH",
"comment": "<comment>"
}
]

Nếu không có lỗi:

[]
`;

      ````;
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

      let reviews = [];

      try {
        reviews = JSON.parse(reviewText);
      } catch (e) {
        console.log(`⚠️ Gemini trả JSON không hợp lệ cho ${file.filename}`);
        continue;
      }

      if (!reviews.length) {
        console.log(`✅ LGTM ${file.filename}`);
        continue;
      }

      const patchLines = file.patch.split("\n");

      const comments = [];

      for (const review of reviews) {
        const snippet = review.codeSnippet;

        if (!snippet) continue;

        let patchIndex = patchLines.findIndex(
          (l) =>
            l.includes(snippet) && (l.startsWith("+") || l.startsWith(" ")),
        );

        if (patchIndex === -1) {
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

        comments.push({
          path: file.filename,
          line: currentLine,
          side: "RIGHT",
          body: `🤖 AI Review (${review.severity})\n\n` + review.comment,
        });
      }

      if (comments.length) {
        await github.rest.pulls.createReview({
          owner: context.repo.owner,
          repo: context.repo.repo,
          pull_number: prNumber,
          event: "COMMENT",
          comments,
        });

        console.log(
          `✅ Đã tạo ${comments.length} review comments cho ${file.filename}`,
        );
      }
    } catch (error) {
      console.error(`❌ Review lỗi file ${file.filename}`, error);
    }
    ````;
  }

  console.log("🎉 Hoàn tất AI Review.");
}

module.exports = run;
