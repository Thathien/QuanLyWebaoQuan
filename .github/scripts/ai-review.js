const fs = require('fs');

async function run({ github, context }) {
  const apiKey = process.env.GEMINI_API_KEY;
  if (!apiKey) {
    console.warn("⚠️ Không tìm thấy GEMINI_API_KEY. Vui lòng cấu hình Secret này trong GitHub Repository Settings.");
    return;
  }

  const diffPath = 'diff.txt';
  if (!fs.existsSync(diffPath)) {
    console.error("❌ Không tìm thấy file diff.txt.");
    return;
  }

  let diff = fs.readFileSync(diffPath, 'utf8').trim();
  if (!diff) {
    console.log("ℹ️ Không phát hiện thay đổi nào để review.");
    return;
  }

  // Giới hạn kích thước diff (khoảng 30K ký tự ~ 8K tokens) để tránh vượt quá giới hạn API
  const MAX_DIFF_LENGTH = 30000;
  if (diff.length > MAX_DIFF_LENGTH) {
    diff = diff.substring(0, MAX_DIFF_LENGTH) + "\n\n... [Diff bị rút ngắn do quá dài] ...";
  }

  console.log("🤖 Đang gửi diff lên Google Gemini API để thực hiện Code Review...");

  try {
    const prompt = `Bạn là một kỹ sư phần mềm cao cấp và chuyên gia duyệt code (AI Code Reviewer). 
Hãy nhận xét thay đổi trong Git Diff dưới đây của một dự án Spring Boot 3.5 (Java 21, JPA/Hibernate, JSP).
Tập trung vào:
1. Lỗi logic hoặc bug tiềm ẩn.
2. Các vấn đề kiến trúc (Controller, Service, DAO, Entity).
3. Vấn đề bảo mật (SQL Injection, XSS, rò rỉ dữ liệu).
4. Hiệu năng truy vấn JPA/SQL và cấu trúc dữ liệu.
5. Code style và clean code.

Viết đánh giá bằng tiếng Việt, súc tích, chuyên nghiệp. Sử dụng các emoji và định dạng Markdown (bảng biểu, code block) để làm nổi bật các điểm quan trọng.
Nếu có code đề xuất sửa đổi, hãy viết khối code đề xuất rõ ràng.

Dưới đây là Git Diff:
\`\`\`diff
${diff}
\`\`\``;

    const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        contents: [{
          parts: [{ text: prompt }]
        }]
      })
    });

    if (!response.ok) {
      throw new Error(`Gemini API trả về trạng thái lỗi: ${response.status} ${response.statusText}`);
    }

    const data = await response.json();
    if (!data.candidates || data.candidates.length === 0 || !data.candidates[0].content) {
      throw new Error("Không nhận được kết quả hợp lệ từ Gemini API.");
    }

    const reviewText = data.candidates[0].content.parts[0].text;

    // Đăng nhận xét lên Pull Request dưới dạng comment
    await github.rest.issues.createComment({
      owner: context.repo.owner,
      repo: context.repo.repo,
      issue_number: context.issue.number,
      body: `### 🤖 AI Code Review (Gemini 2.5 Flash)\n\n${reviewText}\n\n*Review thực hiện tự động bởi CI/CD pipeline.*`
    });

    console.log("✅ Đã gửi phản hồi Code Review lên Pull Request thành công!");

  } catch (error) {
    console.error("❌ Lỗi khi thực hiện AI Code Review:", error);
  }
}

module.exports = run;
