<%--
  Created by IntelliJ IDEA.
  User: HACHI
  Date: 15/11/2026
  Time: 5:54 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Cập nhật  đánh giá #${dg.idDanhGia}</h2>

<form action="${pageContext.request.contextPath}/quan-ly-danh-gia/update?id=${dg.idDanhGia}" method="post">
    ID đặt phòng:
    <select name="idDatPhong" required>
        <c:forEach var="dp" items="${listdat_phong}">
            <option value="${dp.idDatPhong}" ${dp.idDatPhong == dg.idDatPhong ? 'selected' : ''}>
                    ${dp.maDatPhong}
            </option>
        </c:forEach>
    </select><br><br>

    ID khách hàng:
    <select name="idKhachHang" required>
        <c:forEach var="kh" items="${listkhach_hang}">
            <option value="${kh.idKhachHang}" ${kh.idKhachHang == dg.idKhachHang ? 'selected' : ''}>
                    ${kh.email}
            </option>
        </c:forEach>
    </select><br><br>
    Điểm đánh giá (1-5): <input type="number" name="diemDanhGia" min="1" max="5" value="${dg.diemDanhGia}" required><br><br>
    Bình luận: <input type="text" name="binhLuan" value="${dg.binhLuan}"><br><br>
    Ẩn đánh giá: <input type="checkbox" name="biAn" ${dg.biAn ? 'checked' : ''}><br><br>
    Lý do ẩn: <input type="text" name="lyDoAn" value="${dg.lyDoAn}"><br><br>

    <button type="submit">Cập nhật</button>
    <a href="${pageContext.request.contextPath}/quan-ly-danh-gia/view">Hủy</a>
</form>
</body>
</html>
