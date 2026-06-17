# 🛠️ Kỹ Năng Chuyên Môn & Tóm Tắt Dự Án (skill.md)

Tài liệu này tóm tắt các nội dung cốt lõi của dự án và liệt kê các kỹ năng chuyên môn về phát triển phần mềm được áp dụng trong dự án **QuanLyWebaoQuan**.

---

## 📌 1. Tóm Tắt Nội Dung README.md

Dự án **QuanLyWebaoQuan** là một ứng dụng thương mại điện tử hoàn chỉnh viết bằng ngôn ngữ **Java 21** trên nền tảng **Spring Boot 3.5.12**, phục vụ việc mua bán quần áo thời trang trực tuyến với 2 phân hệ chính:
*   **Phân hệ Người dùng (User):** Duyệt sản phẩm, xem chi tiết (size, màu sắc), giỏ hàng, đặt hàng, đăng ký/đăng nhập tài khoản và xác thực qua Email.
*   **Phân hệ Quản trị (Admin):** Quản lý sản phẩm (CRUD), quản lý tài khoản nhân viên, cấu hình khuyến mãi, cập nhật trạng thái kho hàng.

### Các Điểm Chính Từ README:
*   **Kiến trúc:** Phân tầng rõ rệt: Entity -> Imp (Interfaces) -> DAO (EntityManager) -> Service -> Controller.
*   **Cơ sở dữ liệu:** Sử dụng MS SQL Server, kết nối bằng JDBC Driver, tự động đồng bộ cấu hình qua Hibernate DDL.
*   **Giao diện:** Sự kết hợp giữa JSP (JSTL) cho rendering động và thư viện CKEditor cho soạn thảo nội dung phong phú.
*   **Kiểm thử:** Tích hợp bộ thư viện JUnit 5, Mockito, MockMvc và H2 Database cho phép thực thi unit test toàn bộ các tầng trong dự án.
*   **Cách chạy:** Cài đặt JDK 21, thiết lập CSDL SQL Server và chạy lệnh `mvn clean spring-boot:run`. Để chạy test: `mvn test`.

---

## 💡 2. Các Kỹ Năng Kỹ Thuật Được Áp Dụng (Technical Skills)

Dự án này là minh chứng rõ ràng cho các kỹ năng lập trình hướng đối tượng, thiết kế hệ thống và phát triển web hiện đại:

### A. Phát Triển Backend (Spring Boot & Java)
*   **Spring Boot Core & Web MVC:** Tổ chức ứng dụng chuẩn mô hình Model-View-Controller, định tuyến request và mapping dữ liệu giữa client-server.
*   **Java 21:** Ứng dụng các tính năng mới của Java như khối văn bản đa dòng (Text Blocks `"""..."""`) phục vụ câu truy vấn SQL/HQL.
*   **Spring Mail:** Cấu hình và sử dụng `JavaMailSender` để thực hiện gửi email xác nhận tài khoản tự động tới người dùng.
*   **Validation:** Xác thực dữ liệu đầu vào phía backend bằng `@Valid` và các ràng buộc như `@NotNull`, `@Size` nhằm đảm bảo tính toàn vẹn thông tin trước khi lưu trữ vào CSDL.

### B. Quản Trị Cơ Sở Dữ Liệu & Persistence Layer
*   **JPA / Hibernate (Entity Manager):**
    *   Tự thiết kế lớp DAO sử dụng `@PersistenceContext private EntityManager entityManager` để tùy biến tối đa câu lệnh truy vấn HQL/JPQL.
    *   Cấu hình cơ chế giao dịch và đồng bộ bằng `@Transactional` (quản lý phân tầng `readOnly` để tối ưu hóa hiệu năng đọc dữ liệu).
*   **Thiết Kế Khóa Phức Hợp (Composite Keys):**
    *   Ứng dụng hiệu quả `@Embeddable` và `@EmbeddedId` đối với lớp `ChiTietHoaDonId`, giải quyết bài toán quan hệ nhiều-nhiều (N-N) trong CSDL giữa hóa đơn và chi tiết sản phẩm.
*   **SQL Server Integration:** Tối ưu liên kết khóa ngoại (`@ManyToOne`, `@OneToMany`) và quản lý quan hệ thực thể chặt chẽ giữa: Sản phẩm, Chi tiết sản phẩm (Size & Màu), Danh mục, Khách hàng, Giỏ hàng, Hóa đơn và Khuyến mãi.

### C. Phát Triển Frontend & Template Engine
*   **JavaServer Pages (JSP) & JSTL:**
    *   Sử dụng JSTL (`<c:forEach>`, `<c:if>`, `<c:choose>`) để render giao diện động từ model của Spring.
    *   Thiết kế giao diện dạng lắp ghép modular (chia nhỏ `Header`, `Footer`, `Menu` ở admin và user) để tái sử dụng tối đa code HTML.
*   **Tích hợp CKEditor:** Kết nối trình soạn thảo văn bản giàu định dạng (Rich Text Editor) cho phép admin dễ dàng định dạng mô tả sản phẩm.

### D. Kiểm Thử Phần Mềm (Software Testing / Unit Test)
*   **Kiểm thử Lớp Độc lập (Unit Test POJOs):** Viết unit test thuần túy bằng JUnit 5 cho các POJO và lớp thực thể chứa logic so sánh (như `equals` và `hashCode` tại [ChiTietHoaDonIdTest](file:///D:/Developer/Data-VSCode/QuanLyWebaoQuan/src/test/java/com/banhang/entity/ChiTietHoaDonIdTest.java)).
*   **Giả Lập Nghiệp Vụ (Service Mocking):** Dùng Mockito (`@Mock`, `@InjectMocks`, `MockitoExtension`) để cô lập tầng DAO, mô phỏng các hành vi nghiệp vụ của lớp [SanPhamServiceTest](file:///D:/Developer/Data-VSCode/QuanLyWebaoQuan/src/test/java/com/banhang/service/SanPhamServiceTest.java) mà không cần khởi động toàn bộ server hay truy cập CSDL thực tế.
*   **Kiểm Thử Tích Hợp Web (MockMvc Web Testing):** Áp dụng `@WebMvcTest` và bộ thư viện `MockMvc` tại [TrangChuUserControllerTest](file:///D:/Developer/Data-VSCode/QuanLyWebaoQuan/src/test/java/com/banhang/controller/user/TrangChuUserControllerTest.java) nhằm kiểm thử định tuyến HTTP, trạng thái Response (200 OK, 3xx Redirect), kiểm tra view trả về, và thuộc tính dữ liệu chứa trong ModelMap.
*   **Cơ Sở Dữ Liệu Kiểm Thử (In-Memory Database):** Tích hợp H2 Database làm CSDL ảo hỗ trợ việc chạy kiểm thử nhanh chóng và độc lập với môi trường CSDL vật lý (SQL Server).

### E. Tiêu Chuẩn Mã Nguồn & Công Cụ (Clean Code & Tooling)
*   **Project Lombok:** Áp dụng `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` để giảm tối đa số dòng code rác (boilerplate code), giúp mã nguồn dễ đọc hơn.
*   **Maven Build System:** Quản lý vòng đời dự án (Build lifecycle), đóng gói ứng dụng dạng `.war` để triển khai trên các Servlet Container độc lập (như Apache Tomcat).
*   **Spotless & Google Java Format:** Định dạng code tự động đồng bộ theo chuẩn chung của Google, duy trì tính thẩm mỹ và đồng nhất của mã nguồn.
*   **Logging:** Ghi log hệ thống bằng `java.util.logging.Logger` ở tầng DAO để kiểm soát các lỗi phát sinh trong quá trình truy vấn dữ liệu.

### F. Quy Trình CI/CD & Tự Động Hóa (DevOps & CI/CD Automation)
*   **GitHub Actions Pipelines:** Tự thiết kế và cấu hình quy trình CI/CD tích hợp liên tục tự động ([ci-cd.yml](file:///D:/Developer/Data-VSCode/QuanLyWebaoQuan/.github/workflows/ci-cd.yml)) xử lý định dạng mã nguồn, build, kiểm thử tự động trên máy chủ Ubuntu.
*   **Auto Formatting Commit Back:** Tích hợp công cụ tự động phát hiện mã nguồn chưa chuẩn format bằng Spotless, thực thi `spotless:apply` và tự động commit/push ngược về nhánh phát triển thông qua Git Auto Commit Action.
*   **Tích hợp Trí Tuệ Nhân Tạo (Gemini AI Integration):** Xây dựng kịch bản Node.js tùy chỉnh ([ai-review.js](file:///D:/Developer/Data-VSCode/QuanLyWebaoQuan/.github/scripts/ai-review.js)) để thu thập thông tin thay đổi mã nguồn qua Git Diff, gửi yêu cầu đánh giá bảo mật, hiệu năng, kiến trúc sang Google Gemini API (model `gemini-2.5-flash`), và tự động đẩy đánh giá dưới dạng bình luận trên Pull Request của GitHub.
*   **Tự động dọn dẹp mã nguồn (Branch Cleanup Automation):** Lập trình chức năng gọi API GitHub tự động phát hiện các Pull Request đã đóng ở trạng thái merge thành công để xóa các nhánh phụ (feature branch), đồng thời cấu hình bảo vệ các nhánh chính (`main`, `master`, `develop`, `release`).
*   **Quản trị dự án & Bảo mật nhánh (Repository Governance & Branch Protection):** Thiết lập quy tắc chặn commit trực tiếp lên các nhánh chính (`main`, `develop`), yêu cầu các thay đổi phải thông qua Pull Request (PR) và buộc toàn bộ các bài kiểm thử tự động (CI status checks) phải vượt qua thành công trước khi merge.



