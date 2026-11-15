<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập Nhật Thông Tin Phòng ${phong.maPhong} — The Royal Hotel</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">

    <style>
        html, body {
            margin: 0;
            padding: 0;
            width: 100%;
            overflow-x: hidden;
            background: #f7f9f8;
        }
        .app-shell {
            display: flex;
            width: 100%;
            max-width: 100%;
            min-height: 100vh;
            overflow-x: hidden;
            background: #f7f9f8;
            box-sizing: border-box;
        }
        .main-area {
            margin-left: 260px;
            min-height: 100vh;
            flex: 1;
            width: calc(100% - 260px);
            max-width: calc(100% - 260px);
            display: flex;
            flex-direction: column;
            overflow-x: hidden;
            box-sizing: border-box;
            background: #f7f9f8;
        }
        .content {
            padding: 24px 32px 48px;
            flex: 1;
            max-width: 100%;
            box-sizing: border-box;
            overflow-x: hidden;
        }
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 20px;
            margin-bottom: 24px;
        }
        .form-group label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            color: #2b3a33;
            margin-bottom: 6px;
        }
        .form-control {
            width: 100%;
            padding: 10px 14px;
            border: 1px solid #d4ddd7;
            border-radius: 8px;
            font-size: 13px;
            background: #fff;
            color: #222831;
            outline: none;
            transition: all 0.2s ease;
        }
        .form-control:focus {
            border-color: #557c56;
            box-shadow: 0 0 0 3px rgba(85, 124, 86, 0.15);
        }
    </style>
</head>

<body>
    <div class="app-shell">
        <!-- 1. SIDEBAR DÙNG CHUNG -->
        <jsp:include page="/layout/sidebar.jsp">
            <jsp:param name="activeMenu" value="phong" />
        </jsp:include>

        <!-- 2. MAIN AREA -->
        <main class="main-area">
            <!-- HEADER DÙNG CHUNG -->
            <jsp:include page="/layout/header.jsp">
                <jsp:param name="pageTitle" value="Cập Nhật Phòng ${phong.maPhong}" />
            </jsp:include>

            <section class="content" style="padding: 28px;">
                <div class="page-heading">
                    <div>
                        <div class="eyebrow" style="font-size: 11px; font-weight: 700; color: #557c56; letter-spacing: 2px;">CHỈNH SỬA THÔNG TIN PHÒNG</div>
                        <h1 style="font-size: 24px; font-weight: 800; color: #222831; margin: 4px 0;">Cập Nhật Phòng ${phong.maPhong}</h1>
                        <p style="color: #6b7a70; font-size: 13px; margin: 0;">Cập nhật số phòng, vị trí tầng, phân loại tầm nhìn hoặc đổi trạng thái phòng vận hành.</p>
                    </div>
                    <div class="heading-actions">
                        <a href="${pageContext.request.contextPath}/quan-ly-phong/view" class="btn btn-ghost">
                            <i class='bx bx-arrow-back'></i> Quay lại danh sách
                        </a>
                    </div>
                </div>

                <div class="card">
                    <form action="${pageContext.request.contextPath}/quan-ly-phong/update" method="post">
                        <input type="hidden" name="id" value="${phong.idPhong}">

                        <div class="form-grid">
                            <div class="form-group">
                                <label>Mã phòng <span style="color:red">*</span></label>
                                <input type="text" name="maPhong" class="form-control" value="${phong.maPhong}" required>
                            </div>

                            <div class="form-group">
                                <label>Tòa nhà & Tầng <span style="color:red">*</span></label>
                                <select name="idTang" class="form-control" required>
                                    <c:forEach items="${listTang}" var="tang">
                                        <c:set var="tNha" value="${mapToaNha[tang.idToaNha]}" />
                                        <option value="${tang.idTang}" ${tang.idTang == phong.idTang ? 'selected' : ''}>
                                            Tòa ${tNha != null ? tNha.ten : 'Chung'} — Tầng ${tang.soTang}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Loại phòng & Đơn giá <span style="color:red">*</span></label>
                                <select name="idLoaiPhong" class="form-control" required>
                                    <c:forEach items="${listLoaiPhong}" var="lp">
                                        <option value="${lp.idLoaiPhong}" ${lp.idLoaiPhong == phong.idLoaiPhong ? 'selected' : ''}>
                                            ${lp.ten} (<fmt:formatNumber value="${lp.giaMoiDem}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>/đêm)
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Tầm nhìn (View) <span style="color:red">*</span></label>
                                <select name="ghiChu" class="form-control" required>
                                    <option value="Ocean View" ${phong.ghiChu == 'Ocean View' ? 'selected' : ''}>🌊 Ocean View (Hướng biển)</option>
                                    <option value="City View" ${phong.ghiChu == 'City View' ? 'selected' : ''}>🏙️ City View (Hướng thành phố)</option>
                                    <option value="Mountain View" ${phong.ghiChu == 'Mountain View' ? 'selected' : ''}>⛰️ Mountain View (Hướng núi)</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Trạng thái phòng</label>
                                <select name="trangThai" class="form-control">
                                    <option value="SAN_SANG" ${phong.trangThai == 'SAN_SANG' ? 'selected' : ''}>Sẵn sàng đón khách</option>
                                    <option value="DANG_O" ${phong.trangThai == 'DANG_O' ? 'selected' : ''}>Đang ở</option>
                                    <option value="CAN_DON" ${phong.trangThai == 'CAN_DON' ? 'selected' : ''}>Cần dọn</option>
                                    <option value="BAO_TRI" ${phong.trangThai == 'BAO_TRI' ? 'selected' : ''}>Bảo trì kỹ thuật</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Đường dẫn hình ảnh</label>
                                <input type="text" name="hinhAnhPhong" class="form-control" value="${phong.hinhAnhPhong}">
                            </div>
                        </div>

                        <div style="display: flex; justify-content: flex-end; gap: 12px; border-top: 1px solid #eef2ef; padding-top: 18px;">
                            <a href="${pageContext.request.contextPath}/quan-ly-phong/view" class="btn btn-ghost">Hủy bỏ</a>
                            <button type="submit" class="btn btn-primary"><i class='bx bx-save'></i> Lưu Cập Nhật</button>
                        </div>
                    </form>
                </div>
            </section>

            <!-- FOOTER DÙNG CHUNG -->
            <jsp:include page="/layout/footer.jsp" />
        </main>
    </div>
</body>
</html>
