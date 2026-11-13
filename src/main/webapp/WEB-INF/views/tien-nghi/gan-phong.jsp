<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Tiện nghi của phòng"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-head"><h1>Tiện nghi của phòng <c:out value="${phong.maPhong}"/></h1></div>

<form class="panel" method="post" action="${ctx}/tien-nghi">
  <input type="hidden" name="action" value="ganPhong">
  <input type="hidden" name="idPhong" value="${phong.id}">

  <c:choose>
    <c:when test="${empty dsTatCaTienNghi}">
      <p class="empty">Chưa có tiện nghi nào để gán. <a href="${ctx}/tien-nghi?action=form">Thêm tiện nghi</a> trước.</p>
    </c:when>
    <c:otherwise>
      <p class="hint">Tick những tiện nghi phòng này có, rồi bấm Lưu.</p>
      <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(15rem,1fr));gap:.6rem 1.5rem;margin:1rem 0 1.5rem">
        <c:forEach var="t" items="${dsTatCaTienNghi}">
          <label style="flex-direction:row;align-items:center;gap:.6rem;color:var(--ink);font-size:1rem">
            <input type="checkbox" name="idTienNghi" value="${t.id}" ${idDaGan.contains(t.id) ? 'checked' : ''}>
            <c:out value="${t.tenTienNghi}"/>
          </label>
        </c:forEach>
      </div>
      <div class="actions">
        <button class="btn primary" type="submit">Lưu tiện nghi</button>
        <a class="btn" href="${ctx}/tien-nghi">Quay lại</a>
      </div>
    </c:otherwise>
  </c:choose>
</form>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
