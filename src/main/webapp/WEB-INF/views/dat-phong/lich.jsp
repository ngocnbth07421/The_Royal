<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Lịch đặt phòng"/>
<c:set var="menu" value="don"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-heading">
    <div>
        <div class="eyebrow">VẬN HÀNH KHÁCH SẠN</div>
        <h1>Lịch đặt phòng</h1>
        <p>Mỗi thanh trên lịch là một lượt ở: bắt đầu từ ngày nhận phòng, kết thúc vào ngày trả phòng. Bấm vào thanh để xem chi tiết.</p>
    </div>
    <div class="heading-actions">
        <div class="seg">
            <a href="${ctx}/dat-phong"><i class='bx bx-list-ul'></i> Danh sách</a>
            <a class="active" href="${ctx}/dat-phong?action=lich"><i class='bx bx-calendar'></i> Lịch</a>
        </div>
        <a class="btn btn-primary" href="${ctx}/dat-phong?action=timPhong"><i class='bx bx-plus'></i> Tạo đặt phòng mới</a>
    </div>
</div>

<div class="panel" style="margin-bottom:18px">
    <div class="filter-bar">
        <div class="field">
            <label for="locPhong">Mã phòng</label>
            <input id="locPhong" type="search" placeholder="Ví dụ: C0101">
        </div>
        <div class="field">
            <label for="locTrangThai">Trạng thái đơn</label>
            <select id="locTrangThai">
                <option value="">Tất cả</option>
                <option value="CHO_COC">Chờ cọc</option>
                <option value="DA_XAC_NHAN">Đã xác nhận</option>
                <option value="DANG_LUU_TRU">Đang lưu trú</option>
                <option value="DA_TRA_PHONG">Đã trả phòng</option>
            </select>
        </div>
        <div class="field" style="flex:0 1 auto;min-width:0">
            <label style="flex-direction:row;align-items:center;gap:8px;font-size:13px;color:var(--ink,#222831)">
                <input id="locHuy" type="checkbox" style="width:16px;height:16px"> Hiện cả đơn đã hủy
            </label>
        </div>
        <button class="btn btn-success" type="button" id="btnLoc"><i class='bx bx-filter-alt'></i> Lọc</button>
    </div>
</div>

<div class="panel">
    <div class="cal-legend" style="margin-bottom:14px">
        <span><i style="background:#fff8e1;border-color:#b78103"></i>Chờ cọc</span>
        <span><i style="background:#e8f5e9;border-color:#2e7d32"></i>Đã xác nhận</span>
        <span><i style="background:#e3f2fd;border-color:#1565c0"></i>Đang lưu trú</span>
        <span><i style="background:#eceff1;border-color:#546e7a"></i>Đã trả phòng</span>
        <span><i style="background:#ffebee;border-color:#c62828"></i>Đã hủy</span>
    </div>
    <div id="calendar"></div>
    <p class="hint" id="calLoi" style="color:var(--color-danger);display:none"></p>
</div>

<!-- Cửa sổ chi tiết khi bấm vào một thanh trên lịch -->
<div class="modal-backdrop" id="calModal">
    <div class="modal">
        <div class="modal-head">
            <div class="modal-head-title">
                <div class="eyebrow">CHI TIẾT LƯỢT Ở</div>
                <h2 id="mMa">—</h2>
            </div>
            <button class="close-modal" id="mDong" type="button" aria-label="Đóng"><i class='bx bx-x'></i></button>
        </div>
        <div class="modal-body">
            <dl class="facts" id="mFacts"></dl>
        </div>
        <div class="modal-actions">
            <button class="btn btn-ghost" id="mDong2" type="button">Đóng</button>
            <a class="btn btn-success" id="mLink" href="#"><i class='bx bx-right-arrow-alt'></i> Xem chi tiết đơn</a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@fullcalendar/core@6.1.15/locales/vi.global.min.js" onerror="void 0"></script>
<script>
(function () {
    var ctx = '${ctx}';
    var calEl = document.getElementById('calendar');
    var loiEl = document.getElementById('calLoi');
    var modal = document.getElementById('calModal');

    function dongModal() { modal.classList.remove('show'); }
    document.getElementById('mDong').addEventListener('click', dongModal);
    document.getElementById('mDong2').addEventListener('click', dongModal);
    modal.addEventListener('click', function (e) { if (e.target === modal) dongModal(); });

    function esc(v) { var d = document.createElement('div'); d.textContent = v == null ? '' : v; return d.innerHTML; }

    var cal = new FullCalendar.Calendar(calEl, {
        initialView: 'dayGridMonth',
        locale: 'vi',
        firstDay: 1,
        height: 'auto',
        dayMaxEvents: 4,
        headerToolbar: { left: 'prev,next today', center: 'title', right: 'dayGridMonth,dayGridWeek,listMonth' },
        buttonText: { today: 'Hôm nay', month: 'Tháng', week: 'Tuần', list: 'Danh sách' },
        noEventsText: 'Không có lượt nhận hoặc trả phòng trong khoảng này',
        moreLinkText: function (n) { return '+' + n + ' nữa'; },
        events: function (info, ok, fail) {
            var url = ctx + '/dat-phong?action=lichDuLieu'
                + '&start=' + encodeURIComponent(info.startStr.substring(0, 10))
                + '&end=' + encodeURIComponent(info.endStr.substring(0, 10))
                + '&maPhong=' + encodeURIComponent(document.getElementById('locPhong').value.trim())
                + '&trangThai=' + encodeURIComponent(document.getElementById('locTrangThai').value)
                + '&goiHuy=' + (document.getElementById('locHuy').checked ? '1' : '0');
            fetch(url, { headers: { 'Accept': 'application/json' } })
                .then(function (r) { return r.json().then(function (j) { if (!r.ok) throw new Error(j.loi || 'Lỗi tải lịch'); return j; }); })
                .then(function (ds) { loiEl.style.display = 'none'; ok(ds); })
                .catch(function (e) { loiEl.textContent = e.message; loiEl.style.display = 'block'; fail(e); });
        },
        eventClick: function (info) {
            info.jsEvent.preventDefault();
            var p = info.event.extendedProps;
            document.getElementById('mMa').textContent = p.maDatPhong + ' · Phòng ' + p.phong;
            document.getElementById('mFacts').innerHTML =
                '<div><dt>Khách hàng</dt><dd>' + esc(p.khach) + '</dd></div>' +
                '<div><dt>Số điện thoại</dt><dd>' + esc(p.sdt) + '</dd></div>' +
                '<div><dt>Nhận phòng</dt><dd>' + esc(p.nhan) + '</dd></div>' +
                '<div><dt>Trả phòng</dt><dd>' + esc(p.tra) + '</dd></div>' +
                '<div><dt>Số người</dt><dd>' + p.nguoiLon + ' người lớn, ' + p.treEm + ' trẻ em</dd></div>' +
                '<div><dt>Trạng thái đơn</dt><dd>' + esc(p.trangThai) + '</dd></div>';
            document.getElementById('mLink').href = ctx + '/dat-phong?action=chiTiet&id=' + p.idDatPhong;
            modal.classList.add('show');
        }
    });
    cal.render();

    document.getElementById('btnLoc').addEventListener('click', function () { cal.refetchEvents(); });
    document.getElementById('locPhong').addEventListener('keydown', function (e) { if (e.key === 'Enter') cal.refetchEvents(); });
})();
</script>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
