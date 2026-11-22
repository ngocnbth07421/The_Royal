/**
 * =============================================================================
 * THE ROYAL HOTEL — HỆ THỐNG QUẢN TRỊ KHÁCH SẠN HOÀNG GIA (ADMIN JAVASCRIPT)
 * =============================================================================
 * Kiến trúc:
 * - 2 tòa nhà: Clarence và Windsor.
 * - Mỗi tòa 30 tầng (Tầng 1 đến Tầng 30).
 * - Mỗi tầng 5 phòng (Phòng 1 đến Phòng 5). Tổng cộng: 300 phòng.
 * - Phân loại phòng:
 *   + Chất lượng: Standard Room, Deluxe Room, Suite Room, Presidential Suite.
 *   + Tầm nhìn (View): City View, Ocean View, Mountain View (Không phụ thu).
 *   + Sức chứa: Single Room (1 ng), Twin Room (2 ng), Double Room (2 ng),
 *               Triple Room (3 ng), Family Room (4 ng).
 *   + Chính sách trẻ em: Thêm tối đa 2 trẻ em < 6 tuổi (bé 1 miễn phí, bé 2 phụ thu 5%/đêm).
 *                       Trẻ >= 6 tuổi tính như 1 người lớn.
 *   + Tiện nghi đi kèm: Quản lý có thể setup tiện nghi cho từng phòng riêng biệt.
 *
 * LƯU Ý DÀNH CHO CÁC BẠN CODE BACKEND JAVA (INTELLIJI IDEA / SPRING BOOT):
 * - Toàn bộ dữ liệu nghiệp vụ (Bookings, Customers, Staff...) đã được dọn sạch.
 * - Các biến db.* được để mảng rỗng `[]` sẵn sàng để các bạn nạp từ API REST / CSDL MySQL.
 * - Hệ thống tự động nhận diện nếu chạy cùng Java Backend (Spring Boot trên cổng 8080).
 * =============================================================================
 */

// Hàm viết tắt truy vấn DOM
const $ = selector => document.querySelector(selector);
const $$ = selector => document.querySelectorAll(selector);

// =============================================================================
// 1. DANH MỤC TIÊU CHUẨN CỦA HỆ THỐNG (MASTER DATA & SPECIFICATIONS)
// =============================================================================

// Danh mục tiện nghi tiêu chuẩn với biểu tượng Boxicons (https://boxicons.com)
const MASTER_AMENITIES = [
    { id: 'wifi', name: 'Wi-Fi 6 tốc độ cao', icon: '<i class="bx bx-wifi"></i>' },
    { id: 'smart_tv', name: 'Smart TV 65" 4K OLED', icon: '<i class="bx bx-tv"></i>' },
    { id: 'air_cond', name: 'Điều hòa 2 chiều Inverter lọc bụi mịn', icon: '<i class="bx bx-wind"></i>' },
    { id: 'bathtub', name: 'Bồn tắm nằm cao cấp & Sục Jacuzzi', icon: '<i class="bx bx-bath"></i>' },
    { id: 'hairdryer', name: 'Máy sấy tóc ion âm cao cấp', icon: '<i class="bx bx-wind"></i>' },
    { id: 'safe_box', name: 'Két sắt an toàn điện tử', icon: '<i class="bx bx-lock-alt"></i>' },
    { id: 'minibar', name: 'Minibar & Đồ uống miễn phí', icon: '<i class="bx bx-drink"></i>' },
    { id: 'balcony', name: 'Ban công ngắm cảnh riêng biệt', icon: '<i class="bx bx-sun"></i>' },
    { id: 'coffee_maker', name: 'Máy pha cà phê Espresso & Trà hảo hạng', icon: '<i class="bx bx-coffee"></i>' },
    { id: 'bathrobe', name: 'Áo choàng tắm lông cừu & Dép êm ái', icon: '<i class="bx bx-closet"></i>' },
    { id: 'work_desk', name: 'Bàn làm việc doanh nhân & Đèn đọc sách', icon: '<i class="bx bx-briefcase-alt-2"></i>' },
    { id: 'sound_system', name: 'Hệ thống loa vòm Bluetooth cao cấp', icon: '<i class="bx bx-speaker"></i>' }
];

// Phân loại theo Chất lượng (Quality)
const ROOM_QUALITIES = {
    'Standard Room': {
        name: 'Standard Room (Phòng tiêu chuẩn)',
        code: 'STD',
        basePrice: 1200000,
        desc: 'Phòng tiện nghi tiêu chuẩn hiện đại, ấm cúng và đầy đủ công năng cơ bản.'
    },
    'Deluxe Room': {
        name: 'Deluxe Room (Phòng cao cấp)',
        code: 'DLX',
        basePrice: 2200000,
        desc: 'Không gian sang trọng, nội thất cao cấp với tầm nhìn thoáng đãng.'
    },
    'Suite Room': {
        name: 'Suite Room (Phòng hạng sang)',
        code: 'SUT',
        basePrice: 4500000,
        desc: 'Phòng khách và phòng ngủ tách biệt, bồn tắm sục Jacuzzi và ban công lớn.'
    },
    'Presidential Suite': {
        name: 'Presidential Suite (Phòng tổng thống)',
        code: 'PRS',
        basePrice: 12000000,
        desc: 'Đẳng cấp thượng lưu bậc nhất với quản gia riêng và tiện nghi hoàng gia trọn vẹn.'
    }
};

// Phân loại theo Tầm nhìn (View)
const ROOM_VIEWS = {
    'City View': { name: 'City View (Hướng thành phố)', surcharge: 0, desc: 'Ngắm nhìn nhịp sống sôi động về đêm của thành phố.' },
    'Ocean View': { name: 'Ocean View (Hướng biển)', surcharge: 0, desc: 'Tầm nhìn trực diện đại dương xanh ngát và đón bình minh.' },
    'Mountain View': { name: 'Mountain View (Hướng núi)', surcharge: 0, desc: 'Không gian yên bình, trong lành hướng về rặng núi hùng vĩ.' }
};

// Phân loại theo Loại giường (Bed Type) - Bảng loai_giuong trong CSDL
const ROOM_CAPACITIES = {
    'Single Room': {
        name: 'Single Room (1 giường đơn - 1 người)',
        bed: '1 giường đơn',
        maxAdults: 1,
        desc: 'Dành cho 1 khách công tác hoặc du lịch cá nhân.'
    },
    'Twin Room': {
        name: 'Twin Room (2 giường đơn - 2 người)',
        bed: '2 giường đơn',
        maxAdults: 2,
        desc: 'Dành cho 2 khách với 2 giường đơn riêng biệt.'
    },
    'Double Room': {
        name: 'Double Room (1 giường đôi - 2 người)',
        bed: '1 giường đôi lớn (King-size)',
        maxAdults: 2,
        desc: 'Dành cho cặp đôi hoặc 2 khách muốn giường đôi rộng rãi.'
    },
    'Triple Room': {
        name: 'Triple Room (3 người)',
        bed: '3 giường đơn HOẶC 1 giường đôi + 1 giường đơn',
        maxAdults: 3,
        desc: 'Dành cho nhóm 3 khách hoặc gia đình nhỏ.'
    },
    'Family Room': {
        name: 'Family Room (4 người)',
        bed: '2 giường đôi HOẶC 1 giường đôi + 2 giường đơn',
        maxAdults: 4,
        desc: 'Không gian rộng rãi lý tưởng cho gia đình 4 người lớn.'
    }
};

// Danh mục các trang giao diện (tiếng Việt không dấu có _)
const pages = {
    tong_quan: ['Tổng quan', 'Chào mừng trở lại! Tình hình vận hành của 2 tòa Clarence & Windsor hôm nay.'],
    phong: ['Quản lý sơ đồ phòng', 'Sơ đồ 300 phòng 2 tòa Clarence & Windsor (30 tầng x 5 phòng/tầng).'],
    dat_phong: ['Quản lý đặt phòng', 'Theo dõi, thêm mới và quản lý đặt phòng với chính sách phụ thu trẻ em.'],
    loai_phong: ['Danh mục loại phòng', 'Tiêu chuẩn phân loại theo chất lượng, view, loại giường và quy định trẻ em.'],
    khach_hang: ['Quản lý khách hàng', 'Lưu trữ hồ sơ, lịch sử lưu trú và hạng thành viên khách hàng.'],
    nhan_vien: ['Quản lý nhân viên', 'Quản lý nhân sự, phân ca trực và phòng ban phục vụ khách sạn.'],
    tien_nghi: ['Danh mục tiện nghi', 'Kho danh mục tiện nghi dùng để thiết lập cấu hình cho từng phòng.'],
    dich_vu: ['Dịch vụ khách sạn', 'Các dịch vụ đi kèm như buffet, đưa đón sân bay, spa, giặt ủi.'],
    thanh_toan: ['Quản lý thanh toán', 'Hóa đơn thanh toán lưu trú, dịch vụ và phương thức thanh toán.'],
    ma_giam_gia: ['Chương trình khuyến mại', 'Mã ưu đãi, chương trình tri ân thành viên và giảm giá.'],
    danh_gia: ['Đánh giá & Phản hồi', 'Ý kiến đóng góp và xếp hạng chất lượng từ khách hàng.']
};

// Thêm bí danh tiếng Anh để tương thích hoàn toàn
pages.dashboard = pages.tong_quan;
pages.rooms = pages.phong;
pages.bookings = pages.dat_phong;
pages.roomtypes = pages.loai_phong;
pages.customers = pages.khach_hang;
pages.staff = pages.nhan_vien;
pages.amenities = pages.tien_nghi;
pages.services = pages.dich_vu;
pages.payments = pages.thanh_toan;
pages.discounts = pages.ma_giam_gia;
pages.reviews = pages.danh_gia;

// Tiêu đề cột các bảng quản lý (tiếng Việt không dấu có _)
const titles = {
    dat_phong: ['Mã Đặt', 'Khách Hàng', 'Điện Thoại', 'Tòa Nhà', 'Phòng', 'Nhận Phòng', 'Trả Phòng', 'Số Khách', 'Phụ Thu Trẻ', 'Tổng Tiền', 'Trạng Thái'],
    khach_hang: ['Họ và Tên', 'Email', 'Điện Thoại', 'Quốc Tịch', 'Ngày Sinh', 'Hạng Thành Viên'],
    nhan_vien: ['Họ và Tên', 'Email', 'Điện Thoại', 'Chức Vụ', 'Ca Làm Việc', 'Trạng Thái'],
    tien_nghi: ['Tên Tiện Nghi', 'Biểu Tượng', 'Khu Vực Trang Bị', 'Trạng Thái Hoạt Động'],
    dich_vu: ['Tên Dịch Vụ', 'Danh Mục', 'Đơn Giá (VNĐ)', 'Đơn Vị Tính', 'Trạng Thái'],
    thanh_toan: ['Mã Hóa Đơn', 'Mã Đặt Phòng', 'Khách Hàng', 'Số Tiền (VNĐ)', 'Phương Thức', 'Ngày Thanh Toán', 'Trạng Thái'],
    ma_giam_gia: ['Mã Giảm Giá', 'Tên Chương Trình', 'Loại Giảm', 'Giá Trị', 'Ngày Bắt Đầu', 'Ngày Kết Thúc', 'Số Lượt Dùng', 'Trạng Thái'],
    danh_gia: ['Khách Hàng', 'Phòng', 'Điểm Đánh Giá', 'Nội Dung Nhận Xét', 'Ngày Đánh Giá', 'Trạng Thái']
};
titles.bookings = titles.dat_phong;
titles.customers = titles.khach_hang;
titles.staff = titles.nhan_vien;
titles.amenities = titles.tien_nghi;
titles.services = titles.dich_vu;
titles.payments = titles.thanh_toan;
titles.discounts = titles.ma_giam_gia;
titles.reviews = titles.danh_gia;

// Cấu trúc Form thêm/sửa cho các chức năng CRUD (tiếng Việt không dấu có _)
const schemas = {
    dat_phong: [
        ['Mã đặt phòng', 'text'],
        ['Khách hàng', 'text'],
        ['Số điện thoại', 'text'],
        ['Tòa nhà', 'select', 'Clarence,Windsor'],
        ['Phòng', 'text'],
        ['Ngày nhận phòng', 'date'],
        ['Ngày trả phòng', 'date'],
        ['Số người lớn', 'number'],
        ['Số trẻ em (<6 tuổi)', 'select', '0,1,2'],
        ['Trạng thái', 'select', 'Chờ xác nhận,Đã xác nhận,Đang lưu trú,Đã hoàn tất,Đã hủy'],
        ['Tổng tiền (VNĐ)', 'number']
    ],
    khach_hang: [
        ['Họ và tên', 'text'],
        ['Email', 'email'],
        ['Số điện thoại', 'text'],
        ['Quốc tịch', 'text'],
        ['Ngày sinh', 'date'],
        ['Hạng thành viên', 'select', 'Standard,Silver,Gold,Platinum']
    ],
    nhan_vien: [
        ['Họ và tên', 'text'],
        ['Email', 'email'],
        ['Số điện thoại', 'text'],
        ['Chức vụ', 'select', 'Quản lý,Lễ tân,Buồng phòng,Kế toán,Kỹ thuật'],
        ['Ca làm việc', 'select', 'Sáng (06:00-14:00),Chiều (14:00-22:00),Tối (22:00-06:00),Hành chính'],
        ['Trạng thái', 'select', 'Đang làm việc,Nghỉ phép,Đã thôi việc']
    ],
    tien_nghi: [
        ['Tên tiện nghi', 'text'],
        ['Biểu tượng (Icon)', 'text'],
        ['Khu vực trang bị', 'select', 'Phòng ngủ,Phòng tắm,Ban công,Phòng khách,Toàn khách sạn'],
        ['Trạng thái hoạt động', 'select', 'Hoạt động tốt,Bảo trì định kỳ,Ngừng cấp phát']
    ],
    dich_vu: [
        ['Tên dịch vụ', 'text'],
        ['Danh mục', 'select', 'Ẩm thực & Nhà hàng,Đưa đón sân bay,Spa & Thư giãn,Giặt là cao cấp,Dịch vụ phòng'],
        ['Đơn giá (VNĐ)', 'number'],
        ['Đơn vị tính', 'text'],
        ['Trạng thái', 'select', 'Đang cung cấp,Tạm ngưng']
    ],
    thanh_toan: [
        ['Mã hóa đơn', 'text'],
        ['Mã đặt phòng', 'text'],
        ['Khách hàng', 'text'],
        ['Số tiền (VNĐ)', 'number'],
        ['Phương thức', 'select', 'Tiền mặt,Chuyển khoản ngân hàng,Thẻ tín dụng / Ghi nợ,Ví điện tử'],
        ['Ngày thanh toán', 'date'],
        ['Trạng thái', 'select', 'Đã thanh toán,Chờ thanh toán,Đã hoàn tiền']
    ],
    ma_giam_gia: [
        ['Mã giảm giá', 'text'],
        ['Tên chương trình', 'text'],
        ['Loại giảm', 'select', 'Phần trăm (%),Số tiền cố định (VNĐ)'],
        ['Giá trị', 'number'],
        ['Ngày bắt đầu', 'date'],
        ['Ngày kết thúc', 'date'],
        ['Số lượt dùng', 'number'],
        ['Trạng thái', 'select', 'Đang áp dụng,Sắp diễn ra,Hết hiệu lực']
    ],
    danh_gia: [
        ['Khách hàng', 'text'],
        ['Phòng', 'text'],
        ['Điểm đánh giá', 'select', '5,4,3,2,1'],
        ['Nội dung nhận xét', 'textarea'],
        ['Ngày đánh giá', 'date'],
        ['Trạng thái', 'select', 'Đã duyệt,Chờ duyệt,Ẩn phản hồi']
    ]
};
schemas.bookings = schemas.dat_phong;
schemas.customers = schemas.khach_hang;
schemas.staff = schemas.nhan_vien;
schemas.amenities = schemas.tien_nghi;
schemas.services = schemas.dich_vu;
schemas.payments = schemas.thanh_toan;
schemas.discounts = schemas.ma_giam_gia;
schemas.reviews = schemas.danh_gia;

// =============================================================================
// 2. KHỞI TẠO 300 PHÒNG (2 TÒA x 30 TẦNG x 5 PHÒNG/TẦNG)
// =============================================================================
const roomsMap = {};

function initRoomsCatalog() {
    const buildings = [
        { name: 'Clarence', prefix: 'C' },
        { name: 'Windsor', prefix: 'W' }
    ];

    buildings.forEach(b => {
        for (let floor = 1; floor <= 30; floor++) {
            for (let num = 1; num <= 5; num++) {
                const floorStr = String(floor).padStart(2, '0');
                const roomCode = `${b.prefix}-${floorStr}0${num}`;

                let quality = 'Standard Room';
                if (floor >= 29) {
                    quality = num <= 2 ? 'Presidential Suite' : 'Suite Room';
                } else if (floor >= 23) {
                    quality = num === 1 ? 'Presidential Suite' : 'Suite Room';
                } else if (floor >= 11) {
                    quality = num <= 2 ? 'Suite Room' : 'Deluxe Room';
                } else {
                    quality = num <= 3 ? 'Deluxe Room' : 'Standard Room';
                }

                let view = 'City View';
                if (num === 1 || num === 2) view = 'Ocean View';
                else if (num === 3 || num === 4) view = 'City View';
                else view = 'Mountain View';

                let capacity = 'Double Room';
                if (num === 1) capacity = 'Double Room';
                else if (num === 2) capacity = 'Twin Room';
                else if (num === 3) capacity = 'Single Room';
                else if (num === 4) capacity = 'Triple Room';
                else capacity = 'Family Room';

                const price = ROOM_QUALITIES[quality].basePrice;

                let defaultAmenities = ['wifi', 'air_cond', 'smart_tv', 'hairdryer'];
                if (quality === 'Deluxe Room') {
                    defaultAmenities.push('bathtub', 'safe_box', 'minibar');
                } else if (quality === 'Suite Room') {
                    defaultAmenities.push('bathtub', 'safe_box', 'minibar', 'balcony', 'coffee_maker');
                } else if (quality === 'Presidential Suite') {
                    defaultAmenities = MASTER_AMENITIES.map(a => a.id);
                }

                roomsMap[roomCode] = {
                    code: roomCode,
                    building: b.name,
                    floor: floor,
                    roomNumber: num,
                    quality: quality,
                    view: view,
                    capacity: capacity,
                    price: price,
                    status: 'available',
                    amenities: defaultAmenities
                };
            }
        }
    });
}

initRoomsCatalog();

// =============================================================================
// 3. KHU VỰC LƯU TRỮ DỮ LIỆU ĐÃ DỌN SẠCH HOÀN TOÀN (DATABASE HOLDER)
// Dành sẵn các mảng rỗng để nạp dữ liệu từ CSDL (the_royal_sqlserver.sql)
// =============================================================================
const db = {
    dat_phong: [],   // Đã dọn sạch cho Java Backend
    khach_hang: [],  // Đã dọn sạch cho Java Backend
    nhan_vien: [],   // Đã dọn sạch cho Java Backend
    phong: [],       // Đã dọn sạch cho Java Backend
    loai_phong: [],  // Đã dọn sạch cho Java Backend
    loai_giuong: [], // Đã dọn sạch cho Java Backend
    tien_nghi: [],   // Đã dọn sạch cho Java Backend
    dich_vu: [],     // Đã dọn sạch cho Java Backend
    thanh_toan: [],  // Đã dọn sạch cho Java Backend
    ma_giam_gia: [], // Đã dọn sạch cho Java Backend
    danh_gia: []     // Đã dọn sạch cho Java Backend
};

// Liên kết tương thích ngược với các key cũ
db.bookings = db.dat_phong;
db.customers = db.khach_hang;
db.staff = db.nhan_vien;
db.amenities = db.tien_nghi;
db.services = db.dich_vu;
db.payments = db.thanh_toan;
db.discounts = db.ma_giam_gia;
db.reviews = db.danh_gia;

// =============================================================================
// 4. BIẾN TRẠNG THÁI ỨNG DỤNG & ĐỒNG BỘ BACKEND JAVA
// =============================================================================
let currentPage = 'tong_quan';
let editingId = null;
let pageNum = 1;
const pageSize = 6;

let selectedBuilding = 'Clarence';
let selectedFloor = 1;
let selectedRoomCode = 'C-0101';

function formatMoney(amount) {
    return Number(amount || 0).toLocaleString('vi-VN') + ' ₫';
}

function getStatusBadge(val) {
    if (!val) return '';
    const v = String(val).trim();
    if (['Đã xác nhận', 'Đã hoàn tất', 'Đã thanh toán', 'Đã duyệt', 'Đang làm việc', 'Hoạt động tốt', 'Đang cung cấp', 'Đang áp dụng', 'available', 'Còn trống'].includes(v)) {
        return `<span class="status status-success"><span class="status-dot"></span>${v === 'available' ? 'Còn trống' : v}</span>`;
    }
    if (['Chờ xác nhận', 'Chờ thanh toán', 'Chờ duyệt', 'Sắp diễn ra', 'Nghỉ phép', 'occupied', 'Đang sử dụng', 'reserved', 'Đã đặt'].includes(v)) {
        return `<span class="status status-warning"><span class="status-dot"></span>${v === 'occupied' ? 'Đang sử dụng' : v === 'reserved' ? 'Đã đặt' : v}</span>`;
    }
    if (['Đã hủy', 'Bảo trì định kỳ', 'Ẩn phản hồi', 'Hết hiệu lực', 'Ngừng cấp phát', 'Đã thôi việc', 'maintenance', 'Bảo trì'].includes(v)) {
        return `<span class="status status-danger"><span class="status-dot"></span>${v === 'maintenance' ? 'Bảo trì' : v}</span>`;
    }
    return `<span class="status status-neutral"><span class="status-dot"></span>${v}</span>`;
}

// Đồng bộ tự động với Java Backend Spring Boot nếu đang chạy
async function syncWithJavaBackend() {
    try {
        const res = await fetch('/api/rooms');
        if (res.ok) {
            const data = await res.json();
            if (Array.isArray(data) && data.length > 0) {
                data.forEach(r => {
                    const code = r.code;
                    const bName = (r.building === 'WINDSOR' || r.building === 'Windsor') ? 'Windsor' : 'Clarence';
                    roomsMap[code] = {
                        code: code,
                        building: bName,
                        floor: r.floor,
                        roomNumber: r.roomNumber,
                        quality: r.quality ? r.quality.replace('_', ' ') : 'Standard Room',
                        view: r.view ? r.view.replace('_', ' ') : 'City View',
                        capacity: r.capacity ? r.capacity.replace('_', ' ') : 'Double Room',
                        price: r.price,
                        status: (r.status || 'available').toLowerCase(),
                        amenities: r.amenities || []
                    };
                });
                console.log('✓ Kết nối thành công Java Backend! Đã đồng bộ 300 phòng từ Spring Boot.');
            }
        }

        // Tải danh sách bookings nếu có từ backend
        const bookRes = await fetch('/api/bookings');
        if (bookRes.ok) {
            const bookings = await bookRes.json();
            if (Array.isArray(bookings)) {
                db.bookings = bookings.map((b, idx) => ({
                    id: idx + 1,
                    values: [
                        b.bookingId,
                        b.customerName,
                        b.phone,
                        b.building,
                        b.roomCode,
                        b.checkInDate,
                        b.checkOutDate,
                        `${b.adultsCount} lớn, ${b.childrenUnder6Count} trẻ`,
                        b.childrenSurcharge,
                        b.totalAmount,
                        b.status
                    ]
                }));
            }
        }
    } catch (e) {
        // Standalone mode - không kết nối server
    }
}

// =============================================================================
// 5. CHÍNH SÁCH TÍNH TIỀN & PHỤ THU TRẺ EM (CHILDREN POLICY LOGIC)
// =============================================================================
function calculateBookingPrice(roomPricePerNight, checkInDate, checkOutDate, adultsCount, childrenUnder6Count) {
    const inDate = new Date(checkInDate);
    const outDate = new Date(checkOutDate);
    let diffDays = Math.ceil((outDate - inDate) / (1000 * 60 * 60 * 24));
    if (isNaN(diffDays) || diffDays < 1) diffDays = 1;

    const baseTotal = roomPricePerNight * diffDays;
    let childSurcharge = 0;
    const numChildren = parseInt(childrenUnder6Count, 10) || 0;
    if (numChildren >= 2) {
        childSurcharge = (roomPricePerNight * 0.05) * diffDays;
    }

    const finalTotal = baseTotal + childSurcharge;

    return {
        nights: diffDays,
        baseTotal: baseTotal,
        childSurcharge: childSurcharge,
        finalTotal: finalTotal,
        numChildren: numChildren
    };
}

// =============================================================================
// 6. GIAO DIỆN TỔNG QUAN (DASHBOARD)
// =============================================================================
function renderDashboard() {
    const totalRooms = 300;
    const allRooms = Object.values(roomsMap);
    const occupiedCount = allRooms.filter(r => r.status === 'occupied').length;
    const reservedCount = allRooms.filter(r => r.status === 'reserved').length;
    const maintenanceCount = allRooms.filter(r => r.status === 'maintenance').length;
    const availableCount = totalRooms - occupiedCount - reservedCount - maintenanceCount;

    const occupancyRate = totalRooms > 0 ? Math.round(((occupiedCount + reservedCount) / totalRooms) * 100) : 0;
    const totalBookings = db.bookings.length;

    return `
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-top">TỔNG SỐ PHÒNG 2 TÒA <span class="stat-icon"><i class='bx bx-hotel'></i></span></div>
                <div class="stat-value">300</div>
                <div class="stat-foot">Clarence: 150 · Windsor: 150 (30 tầng)</div>
            </div>
            <div class="stat-card">
                <div class="stat-top">CÔNG SUẤT SỬ DỤNG <span class="stat-icon"><i class='bx bx-pie-chart-alt-2'></i></span></div>
                <div class="stat-value">${occupancyRate}%</div>
                <div class="stat-foot"><span class="positive">${occupiedCount} phòng</span> đang có khách lưu trú</div>
            </div>
            <div class="stat-card">
                <div class="stat-top">PHÒNG TRỐNG SẴN SÀNG <span class="stat-icon"><i class='bx bx-check-circle'></i></span></div>
                <div class="stat-value">${availableCount}</div>
                <div class="stat-foot"><span class="positive">Sẵn sàng</span> phục vụ khách mới ngay</div>
            </div>
            <div class="stat-card">
                <div class="stat-top">ĐẶT PHÒNG TRONG HỆ THỐNG <span class="stat-icon"><i class='bx bx-calendar-event'></i></span></div>
                <div class="stat-value">${totalBookings}</div>
                <div class="stat-foot">${totalBookings === 0 ? 'Dữ liệu đã dọn sạch cho Java' : 'Đang quản lý'}</div>
            </div>
        </div>

        <div class="dashboard-grid">
            <div class="panel">
                <div class="panel-head">
                    <div>
                        <h3>Biểu đồ doanh thu khách sạn (The Royal)</h3>
                        <div class="panel-sub">Dữ liệu doanh thu thực tế các tháng gần nhất (Triệu VNĐ)</div>
                    </div>
                    <select class="filter-select">
                        <option>Năm hiện tại (2026)</option>
                        <option>Năm trước (2025)</option>
                    </select>
                </div>
                <div class="chart-wrap">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>

            <div class="panel">
                <div class="panel-head">
                    <div>
                        <h3>Tình trạng 300 phòng</h3>
                        <div class="panel-sub">Tỷ lệ phân bố 2 tòa Clarence & Windsor</div>
                    </div>
                    <span class="date-chip">Thời gian thực</span>
                </div>
                <div class="occupancy">
                    <div class="donut">
                        <div class="donut-label">
                            <b>${availableCount}</b>
                            <small>Phòng trống</small>
                        </div>
                    </div>
                    <div class="legend">
                        <div class="legend-row">
                            <span style="--dot:#2e7d32">Còn trống</span>
                            <b>${availableCount}</b>
                        </div>
                        <div class="legend-row">
                            <span style="--dot:#b78103">Đang sử dụng</span>
                            <b>${occupiedCount}</b>
                        </div>
                        <div class="legend-row">
                            <span style="--dot:#0277bd">Đã đặt trước</span>
                            <b>${reservedCount}</b>
                        </div>
                        <div class="legend-row">
                            <span style="--dot:#c62828">Bảo trì / Sửa</span>
                            <b>${maintenanceCount}</b>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="bottom-grid">
            <div class="panel">
                <div class="panel-head">
                    <div>
                        <h3>Đặt phòng mới nhất</h3>
                        <div class="panel-sub">Theo dõi các giao dịch gần đây trong hệ thống</div>
                    </div>
                    <button class="btn btn-ghost btn-small" onclick="navigate('dat_phong')">Xem tất cả <i class='bx bx-chevron-right'></i></button>
                </div>
                ${renderMiniRecentBookings()}
            </div>

            <div class="panel">
                <div class="panel-head">
                    <div>
                        <h3>Ghi chú tích hợp Backend Java</h3>
                        <div class="panel-sub">Hướng dẫn cho nhóm lập trình viên</div>
                    </div>
                </div>
                <div class="activity">
                    <div class="activity-row">
                        <div class="activity-dot"><i class='bx bx-check'></i></div>
                        <div>
                            <b>Dữ liệu đã được dọn sạch hoàn toàn</b>
                            <small>Các mảng db.* đã để trống sẵn sàng nhận dữ liệu từ CSDL (the_royal_sqlserver.sql).</small>
                        </div>
                        <time>Hôm nay</time>
                    </div>
                    <div class="activity-row">
                        <div class="activity-dot"><i class='bx bx-hotel'></i></div>
                        <div>
                            <b>Đã cấu hình đủ 300 phòng cho 2 tòa nhà</b>
                            <small>Clarence 30 tầng (150p) & Windsor 30 tầng (150p), mỗi tầng 5 phòng.</small>
                        </div>
                        <time>Hôm nay</time>
                    </div>
                    <div class="activity-row">
                        <div class="activity-dot"><i class='bx bx-child'></i></div>
                        <div>
                            <b>Đã tích hợp công thức phụ thu trẻ em 5%</b>
                            <small>Bé 1 <6t: 0đ; Bé 2: 5%/đêm; Trẻ >= 6t tính 1 người lớn.</small>
                        </div>
                        <time>Hôm nay</time>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function renderMiniRecentBookings() {
    if (!db.dat_phong || db.dat_phong.length === 0) {
        return `
            <div class="empty-box">
                <div class="empty-icon"><i class='bx bx-folder-open' style='font-size:42px;color:#9ea5ab;'></i></div>
                <div class="empty-title">Chưa có giao dịch đặt phòng nào</div>
                <div class="empty-desc">Dữ liệu mẫu đã được dọn sạch. Bạn có thể nhấn nút "＋ Tạo đặt phòng mới" ở góc trên hoặc nạp dữ liệu từ backend Java.</div>
                <button class="btn btn-primary btn-small" onclick="navigate('dat_phong'); openModal('dat_phong')"><i class='bx bx-plus'></i> Tạo đặt phòng mẫu</button>
            </div>
        `;
    }

    return `
        <div class="table-scroll">
            <table>
                <thead>
                    <tr>
                        <th>Mã</th>
                        <th>Khách Hàng</th>
                        <th>Phòng</th>
                        <th>Nhận Phòng</th>
                        <th>Trạng Thái</th>
                    </tr>
                </thead>
                <tbody>
                    ${db.dat_phong.slice(0, 5).map(b => `
                        <tr>
                            <td><b>${b.values[0]}</b></td>
                            <td>${b.values[1]}</td>
                            <td><span class="tag-badge tag-quality">${b.values[4]}</span></td>
                            <td>${b.values[5]}</td>
                            <td>${getStatusBadge(b.values[10] || b.values[7])}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function drawRevenueChart() {
    const el = $('#revenueChart');
    if (!el || !window.Chart) return;
    
    if (window._currentRevenueChart) {
        window._currentRevenueChart.destroy();
    }

    // Dữ liệu ban đầu để trống (0 VNĐ) để nhóm backend nạp từ bảng thanh toán / hóa đơn
    window._currentRevenueChart = new Chart(el, {
        type: 'line',
        data: {
            labels: ['Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11'],
            datasets: [{
                label: 'Doanh thu (Chờ nạp từ CSDL)',
                data: [0, 0, 0, 0, 0, 0],
                borderColor: '#6d9886',
                backgroundColor: 'rgba(109, 152, 134, 0.12)',
                fill: true,
                tension: 0.35,
                pointRadius: 4,
                pointBackgroundColor: '#6d9886',
                borderWidth: 2.5
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: ctx => `Doanh thu: ${ctx.parsed.y} triệu VNĐ`
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: '#eaecea' },
                    ticks: {
                        font: { size: 10 },
                        callback: v => v + ' tr'
                    }
                },
                x: {
                    grid: { display: false },
                    ticks: { font: { size: 10 } }
                }
            }
        }
    });
}

// =============================================================================
// 7. GIAO DIỆN SƠ ĐỒ PHÒNG (ROOM MANAGEMENT MAP)
// =============================================================================
function renderRoomPage() {
    const buildingRooms = Object.values(roomsMap).filter(r => r.building === selectedBuilding);
    const currentFloorRooms = buildingRooms.filter(r => r.floor === selectedFloor);
    const room = roomsMap[selectedRoomCode] || currentFloorRooms[0] || {};

    return `
        <div class="room-controls">
            <div class="building-tabs">
                <button class="building-tab ${selectedBuilding === 'Clarence' ? 'active' : ''}" onclick="switchBuilding('Clarence')">
                    <i class='bx bx-buildings'></i> Tòa Clarence · 150 phòng (30 tầng)
                </button>
                <button class="building-tab ${selectedBuilding === 'Windsor' ? 'active' : ''}" onclick="switchBuilding('Windsor')">
                    <i class='bx bx-building-house'></i> Tòa Windsor · 150 phòng (30 tầng)
                </button>
            </div>

            <div class="room-legend">
                <span><i style="background:#2e7d32"></i>Còn trống</span>
                <span><i style="background:#b78103"></i>Đang sử dụng</span>
                <span><i style="background:#0277bd"></i>Đã đặt trước</span>
                <span><i style="background:#c62828"></i>Bảo trì</span>
            </div>
        </div>

        <div class="room-layout">
            <div class="floor-list">
                <div class="floor-list-head">
                    <h3>Chọn tầng</h3>
                    <span>30 tầng · 5p/tầng</span>
                </div>
                <div class="floor-scroll-container">
                    ${Array.from({ length: 30 }, (_, i) => 30 - i).map(f => `
                        <button class="floor-btn ${selectedFloor === f ? 'active' : ''}" onclick="switchFloor(${f})">
                            <span>Tầng ${String(f).padStart(2, '0')}</span>
                            <small>05 phòng</small>
                        </button>
                    `).join('')}
                </div>
            </div>

            <div class="map-panel">
                <div class="map-head">
                    <div>
                        <h3>Tòa ${selectedBuilding} · Tầng ${String(selectedFloor).padStart(2, '0')}</h3>
                        <div class="panel-sub">Bấm chọn một phòng để xem chi tiết, đổi trạng thái hoặc thiết lập tiện nghi</div>
                    </div>
                    <span class="date-chip">05 phòng / tầng</span>
                </div>

                <div class="room-grid">
                    ${currentFloorRooms.map(r => {
                        const isSelected = r.code === selectedRoomCode;
                        return `
                            <div class="room-tile ${r.status} ${isSelected ? 'selected' : ''}" onclick="pickRoom('${r.code}')">
                                <div class="room-tile-code">${r.code}</div>
                                <span class="room-tile-quality">${r.quality.replace(' Room', '')}</span>
                                <span class="room-tile-view">${r.view}</span>
                                <div class="room-tile-status">
                                    ${r.status === 'available' ? 'Còn trống' : r.status === 'occupied' ? 'Đang ở' : r.status === 'reserved' ? 'Đã đặt' : 'Bảo trì'}
                                </div>
                                <small style="font-size:9.5px;color:#777;margin-top:2px;"><i class='bx bx-spa'></i> ${r.amenities.length} tiện nghi</small>
                            </div>
                        `;
                    }).join('')}
                </div>

                <div class="room-detail-card">
                    <div class="room-detail-header">
                        <div class="room-detail-info">
                            <h4>Phòng ${room.code || 'Chưa chọn phòng'}</h4>
                            <div class="room-detail-tags">
                                <span class="tag-badge tag-quality">${room.quality || 'Standard Room'}</span>
                                <span class="tag-badge tag-view">${room.view || 'City View'}</span>
                                <span class="tag-badge tag-capacity"><i class='bx bx-bed'></i> ${room.capacity || 'Double Room'}</span>
                                ${getStatusBadge(room.status)}
                            </div>
                        </div>
                        <div class="room-detail-price">
                            <b>${formatMoney(room.price)}</b>
                            <small>Giá niêm yết / đêm</small>
                        </div>
                    </div>

                    <div class="policy-banner">
                        <div class="policy-icon"><i class='bx bx-child'></i></div>
                        <div class="policy-text">
                            <b>Chính sách trẻ em đi kèm:</b>
                            <p>Tối đa 2 trẻ em < 6 tuổi trong 1 phòng. Bé thứ 1 miễn phí (0 ₫), bé thứ 2 phụ thu 5% giá phòng / đêm. Trẻ từ 6 tuổi trở lên tính là 1 người lớn.</p>
                        </div>
                    </div>

                    <div class="room-amenities-list">
                        <div class="room-amenities-label">
                            <span><i class='bx bx-spa'></i> Tiện nghi &amp; dịch vụ trang bị đi kèm cho phòng này (${room.amenities ? room.amenities.length : 0} tiện nghi):</span>
                        </div>
                        <div class="room-amenity-pills">
                            ${(room.amenities && room.amenities.length > 0) ? room.amenities.map(aid => {
                                const amenity = MASTER_AMENITIES.find(a => a.id === aid);
                                return `<span class="amenity-pill">${amenity ? amenity.icon + ' ' + amenity.name : aid}</span>`;
                            }).join('') : '<span style="color:#888;font-size:12px;">Chưa thiết lập tiện nghi đi kèm. Nhấn nút "Setup tiện nghi" bên dưới để cấu hình.</span>'}
                        </div>
                    </div>

                    <div class="room-detail-actions">
                        <button class="btn btn-ghost" onclick="toggleRoomStatus('${room.code}')">
                            <i class='bx bx-refresh'></i> Đổi trạng thái: ${room.status === 'available' ? 'Đang sử dụng' : room.status === 'occupied' ? 'Bảo trì' : 'Còn trống'}
                        </button>
                        <button class="btn btn-outline" onclick="openRoomSetupModal('${room.code}')">
                            <i class='bx bx-slider-alt'></i> Thiết lập tiện nghi &amp; Cấu hình phòng
                        </button>
                        <button class="btn btn-primary" onclick="bookThisRoom('${room.code}')" ${room.status === 'available' ? '' : 'title="Phòng hiện không trống"'}>
                            <i class='bx bx-plus'></i> Tạo đặt phòng cho phòng này
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function switchBuilding(building) {
    selectedBuilding = building;
    const prefix = building === 'Clarence' ? 'C' : 'W';
    selectedRoomCode = `${prefix}-${String(selectedFloor).padStart(2, '0')}01`;
    navigate('phong');
}


function switchFloor(floor) {
    selectedFloor = floor;
    const prefix = selectedBuilding === 'Clarence' ? 'C' : 'W';
    selectedRoomCode = `${prefix}-${String(selectedFloor).padStart(2, '0')}01`;
    $('#pageContent').innerHTML = renderRoomPage();
}

function pickRoom(code) {
    selectedRoomCode = code;
    $('#pageContent').innerHTML = renderRoomPage();
}

function toggleRoomStatus(code) {
    const room = roomsMap[code];
    if (!room) return;
    if (room.status === 'available') room.status = 'occupied';
    else if (room.status === 'occupied') room.status = 'maintenance';
    else room.status = 'available';

    // Gửi cập nhật nếu có backend Java
    fetch(`/api/rooms/${code}/status?status=${room.status}`, { method: 'PUT' }).catch(() => {});

    toast(`Đã cập nhật trạng thái phòng ${code} thành: ${room.status === 'available' ? 'Còn trống' : room.status === 'occupied' ? 'Đang sử dụng' : 'Bảo trì'}`);
    $('#pageContent').innerHTML = renderRoomPage();
}

// =============================================================================
// 8. MODAL SETUP TIỆN NGHI & CẤU HÌNH PHÒNG
// =============================================================================
function openRoomSetupModal(code) {
    const room = roomsMap[code];
    if (!room) return;

    $('#setupRoomCode').value = code;
    $('#setupModalTitle').textContent = `Thiết lập cấu hình phòng ${code} (${room.building} - Tầng ${room.floor})`;
    $('#setupQuality').value = room.quality;
    $('#setupView').value = room.view;
    $('#setupCapacity').value = room.capacity;
    $('#setupPrice').value = room.price;
    $('#setupStatus').value = room.status;

    const checkList = $('#amenitiesCheckList');
    checkList.innerHTML = MASTER_AMENITIES.map(a => {
        const isChecked = room.amenities.includes(a.id);
        return `
            <label class="amenity-check-card">
                <input type="checkbox" name="setupAmenities" value="${a.id}" ${isChecked ? 'checked' : ''}>
                <span>${a.icon} ${a.name}</span>
            </label>
        `;
    }).join('');

    $('#roomSetupModal').classList.add('show');
}

$('#closeSetupModal').onclick = $('#cancelSetupModal').onclick = () => {
    $('#roomSetupModal').classList.remove('show');
};

$('#roomSetupModal').onclick = e => {
    if (e.target.id === 'roomSetupModal') $('#roomSetupModal').classList.remove('show');
};

$('#roomSetupForm').onsubmit = e => {
    e.preventDefault();
    const code = $('#setupRoomCode').value;
    const room = roomsMap[code];
    if (!room) return;

    room.quality = $('#setupQuality').value;
    room.view = $('#setupView').value;
    room.capacity = $('#setupCapacity').value;
    room.price = Number($('#setupPrice').value);
    room.status = $('#setupStatus').value;

    const selectedAmenities = Array.from(document.querySelectorAll('input[name="setupAmenities"]:checked')).map(cb => cb.value);
    room.amenities = selectedAmenities;

    // Gửi PUT /api/rooms/{code}/setup lên Spring Boot backend nếu đang chạy
    fetch(`/api/rooms/${code}/setup`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            quality: room.quality,
            view: room.view,
            capacity: room.capacity,
            price: room.price,
            status: room.status,
            amenities: selectedAmenities
        })
    }).catch(() => {});

    $('#roomSetupModal').classList.remove('show');
    toast(`Đã lưu cấu hình và cập nhật ${selectedAmenities.length} tiện nghi cho phòng ${code}!`);
    if (currentPage === 'rooms') {
        $('#pageContent').innerHTML = renderRoomPage();
    }
};

// =============================================================================
// 9. TRANG DANH MỤC LOẠI PHÒNG (ROOM TYPES CATALOG)
// =============================================================================
function renderRoomTypesPage() {
    return `
        <div class="roomtypes-showcase">
            <div class="panel">
                <div class="type-section-title">1. Phân loại theo Chất lượng phòng</div>
                <div class="room-spec-grid">
                    ${Object.entries(ROOM_QUALITIES).map(([k, v]) => `
                        <div class="room-spec-card">
                            <h4>${v.name}</h4>
                            <p>${v.desc}</p>
                            <ul class="room-spec-features">
                                <li>Mã tiêu chuẩn: <b>${v.code}</b></li>
                                <li>Giá tham khảo: <b>${formatMoney(v.basePrice)}/đêm</b></li>
                                <li>Đầy đủ tiện nghi theo tiêu chuẩn 5 sao</li>
                            </ul>
                        </div>
                    `).join('')}
                </div>
            </div>

            <div class="panel">
                <div class="type-section-title">2. Phân loại theo Tầm nhìn (View)</div>
                <div class="room-spec-grid">
                    ${Object.entries(ROOM_VIEWS).map(([k, v]) => `
                        <div class="room-spec-card">
                            <h4>${v.name}</h4>
                            <p>${v.desc}</p>
                            <ul class="room-spec-features">
                                <li>Tầm nhìn: <b>Hướng tiêu chuẩn đón gió tự nhiên</b></li>
                                <li>Cửa sổ kính cường lực cách âm Panorama</li>
                            </ul>
                        </div>
                    `).join('')}
                </div>
            </div>

            <div class="panel">
                <div class="type-section-title">3. Phân loại theo Loại giường (Bảng loai_giuong)</div>
                <div class="room-spec-grid">
                    ${Object.entries(ROOM_CAPACITIES).map(([k, v]) => `
                        <div class="room-spec-card">
                            <h4><i class='bx bx-bed'></i> ${v.name}</h4>
                            <p>${v.desc}</p>
                            <ul class="room-spec-features">
                                <li>Loại giường: <b>${v.bed}</b></li>
                                <li>Sức chứa tiêu chuẩn: <b>${v.maxAdults} người lớn</b></li>
                            </ul>
                        </div>
                    `).join('')}
                </div>
            </div>

            <div class="panel">
                <div class="type-section-title">4. Quy định trẻ em lưu trú tại The Royal Hotel</div>
                <div class="policy-banner" style="margin-top:0;">
                    <div class="policy-icon"><i class='bx bx-child'></i></div>
                    <div class="policy-text">
                        <b>Nguyên tắc tính phụ thu trẻ em:</b>
                        <p>• <b>Thêm tối đa 2 trẻ nhỏ dưới 6 tuổi</b> trong 1 phòng.</p>
                        <p>• <b>Trẻ thứ 1 (< 6 tuổi):</b> Hoàn toàn miễn phí phụ thu (0 ₫).</p>
                        <p>• <b>Trẻ thứ 2 (< 6 tuổi):</b> Phụ thu <b>5% giá phòng / đêm</b>.</p>
                        <p>• <b>Trẻ từ 6 tuổi trở lên:</b> Tính là 1 người (người lớn) và tính vào số lượng khách tiêu chuẩn của phòng.</p>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// =============================================================================
// 10. TRANG QUẢN LÝ DỮ LIỆU CHUNG (TABLE CRUD PAGE)
// =============================================================================
function renderTablePage(key) {
    const rows = db[key] || [];
    const headers = titles[key] || [];

    return `
        <div class="toolbar">
            <div class="toolbar-left">
                <div class="search-wrap">
                    <i class='bx bx-search' style='position:absolute;left:12px;font-size:16px;color:#888;pointer-events:none;z-index:2;'></i>
                    <input class="search-input" id="tableSearch" placeholder="Tìm kiếm trong ${pages[key][0].toLowerCase()}...">
                </div>
                <select class="filter-select" id="tableFilter">
                    <option value="">Tất cả trạng thái</option>
                    ${['Đã xác nhận', 'Chờ xác nhận', 'Đang lưu trú', 'Đã hoàn tất', 'Đã hủy', 'Đã thanh toán', 'Chờ thanh toán', 'Đã duyệt', 'Đang làm việc', 'Hoạt động tốt', 'Đang cung cấp', 'Đang áp dụng'].map(s => `<option value="${s}">${s}</option>`).join('')}
                </select>
            </div>
            <div class="toolbar-right">
                <span class="date-chip">${rows.length} bản ghi</span>
                <button class="btn btn-primary" id="addRecordBtn"><i class='bx bx-plus'></i> Thêm mới ${pages[key][0].toLowerCase()}</button>
            </div>
        </div>

        <div class="table-panel">
            <div class="table-scroll">
                <table>
                    <thead>
                        <tr>
                            ${headers.map(h => `<th>${h}</th>`).join('')}
                            <th style="text-align:right;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="tableBody"></tbody>
                </table>
            </div>

            <div class="table-footer">
                <span id="tableInfo"></span>
                <div class="pagination" id="pagination"></div>
            </div>
        </div>
    `;
}

function renderTableRows() {
    const key = currentPage;
    const rows = db[key] || [];
    const headers = titles[key] || [];
    const q = ($('#tableSearch')?.value || '').toLowerCase().trim();
    const f = $('#tableFilter')?.value || '';

    const filtered = rows.filter(r => {
        const textMatch = r.values.join(' ').toLowerCase().includes(q);
        const filterMatch = !f || r.values.includes(f);
        return textMatch && filterMatch;
    });

    const totalPages = Math.max(1, Math.ceil(filtered.length / pageSize));
    pageNum = Math.min(pageNum, totalPages);
    const slice = filtered.slice((pageNum - 1) * pageSize, pageNum * pageSize);

    const tbody = $('#tableBody');
    if (!tbody) return;

    if (slice.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="${headers.length + 1}">
                    <div class="empty-box">
                        <div class="empty-icon"><i class='bx bx-folder-open' style='font-size:42px;color:#9ea5ab;'></i></div>
                        <div class="empty-title">Chưa có dữ liệu nào trong mục này</div>
                        <div class="empty-desc">
                            Dữ liệu mẫu đã được dọn sạch. Bạn có thể nhấn <b>"＋ Thêm mới"</b> để thêm dữ liệu kiểm thử, hoặc tích hợp API từ Java Spring Boot / Database để nạp dữ liệu vào bảng.
                        </div>
                    </div>
                </td>
            </tr>
        `;
    } else {
        tbody.innerHTML = slice.map(r => `
            <tr>
                ${r.values.map((v, i) => {
                    const header = headers[i] || '';
                    if (header.includes('(VNĐ)') || header.includes('Tổng Tiền') || header.includes('Phụ Thu')) {
                        return `<td><b>${formatMoney(v)}</b></td>`;
                    }
                    if (header.includes('Trạng Thái')) {
                        return `<td>${getStatusBadge(v)}</td>`;
                    }
                    if (header.includes('Hạng')) {
                        return `<td><span class="tag-badge tag-quality">${v}</span></td>`;
                    }
                    if (header === 'Phòng') {
                        return `<td><span class="tag-badge tag-view">${v}</span></td>`;
                    }
                    if (header.includes('Điểm')) {
                        return `<td><span style="color:#c59b27;font-weight:700;"><i class='bx bxs-star'></i> ${v}/5</span></td>`;
                    }
                    return `<td>${String(v).length > 50 ? String(v).slice(0, 48) + '…' : v}</td>`;
                }).join('')}
                <td style="text-align:right;">
                    <div class="actions" style="justify-content:flex-end;">
                        <button class="action-btn" onclick="editRecord(${r.id})" title="Chỉnh sửa"><i class='bx bx-edit-alt'></i></button>
                        <button class="action-btn" onclick="deleteRecord(${r.id})" title="Xóa bản ghi" style="color:#c62828;"><i class='bx bx-trash'></i></button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    $('#tableInfo').textContent = `Hiển thị ${slice.length ? ((pageNum - 1) * pageSize + 1) : 0}–${Math.min(pageNum * pageSize, filtered.length)} trong tổng số ${filtered.length} bản ghi`;

    $('#pagination').innerHTML = Array.from({ length: totalPages }, (_, i) => `
        <button class="page-btn ${pageNum === i + 1 ? 'active' : ''}" onclick="goPage(${i + 1})">${i + 1}</button>
    `).join('');
}

function goPage(n) {
    pageNum = n;
    renderTableRows();
}

// =============================================================================
// 11. MODAL CRUD THÊM / SỬA DỮ LIỆU
// =============================================================================
function openModal(key, id = null, preset = {}) {
    editingId = id;
    const row = id ? db[key].find(x => x.id === id) : null;
    const schema = schemas[key] || [];

    $('#modalTitle').textContent = id ? `Chỉnh sửa ${pages[key][0].toLowerCase()}` : `Thêm mới ${pages[key][0].toLowerCase()}`;

    $('#formFields').innerHTML = schema.map((s, i) => {
        const fieldName = s[0];
        const type = s[1];
        const opts = s[2]?.split(',') || [];
        let val = row ? row.values[i] : (preset[fieldName] || '');

        if (!val && !id) {
            if (fieldName === 'Mã đặt phòng') val = 'BK-' + String(Date.now()).slice(-5);
            if (fieldName === 'Mã hóa đơn') val = 'INV-' + String(Date.now()).slice(-5);
            if (fieldName === 'Phòng' && selectedRoomCode) val = selectedRoomCode;
            if (fieldName === 'Tòa nhà' && selectedBuilding) val = selectedBuilding;
            if (fieldName === 'Ngày nhận phòng') {
                const today = new Date();
                val = today.toISOString().split('T')[0];
            }
            if (fieldName === 'Ngày trả phòng') {
                const tomorrow = new Date();
                tomorrow.setDate(tomorrow.getDate() + 1);
                val = tomorrow.toISOString().split('T')[0];
            }
            if (fieldName === 'Số người lớn') val = 2;
            if (fieldName === 'Số trẻ em (<6 tuổi)') val = 0;
        }

        if (type === 'select') {
            return `
                <div class="form-group">
                    <label>${fieldName}</label>
                    <select name="f${i}" required id="field_${i}">
                        <option value="">-- Chọn ${fieldName} --</option>
                        ${opts.map(o => `<option value="${o}" ${val === o ? 'selected' : ''}>${o}</option>`).join('')}
                    </select>
                </div>
            `;
        }

        if (type === 'textarea') {
            return `
                <div class="form-group full">
                    <label>${fieldName}</label>
                    <textarea name="f${i}" id="field_${i}" placeholder="Nhập ${fieldName.toLowerCase()}...">${val}</textarea>
                </div>
            `;
        }

        return `
            <div class="form-group">
                <label>${fieldName}</label>
                <input name="f${i}" id="field_${i}" type="${type}" value="${val}" ${type === 'number' ? 'min="0"' : ''} required>
            </div>
        `;
    }).join('');

    const breakdownEl = $('#bookingPriceBreakdown');
    if (key === 'bookings') {
        breakdownEl.innerHTML = `
            <div class="pricing-calc-box" id="calcBox">
                <div class="calc-row"><span>Thời gian lưu trú:</span><b id="calcNights">1 đêm</b></div>
                <div class="calc-row"><span>Đơn giá phòng / đêm:</span><span id="calcRoomPrice">...</span></div>
                <div class="calc-row"><span>Phụ thu trẻ em:</span><b id="calcChildSurcharge" style="color:#b78103">0 ₫ (Miễn phí bé 1)</b></div>
                <div class="calc-row total"><span>Tổng số tiền thanh toán:</span><strong id="calcTotal">...</strong></div>
            </div>
        `;
        bindBookingCalculationEvents();
        triggerBookingCalculation();
    } else {
        breakdownEl.innerHTML = '';
    }

    $('#modal').classList.add('show');
}

function bindBookingCalculationEvents() {
    const inDateInput = document.querySelector('input[type="date"][name="f5"]');
    const outDateInput = document.querySelector('input[type="date"][name="f6"]');
    const roomInput = document.querySelector('input[name="f4"]');
    const childSelect = document.querySelector('select[name="f8"]');

    [inDateInput, outDateInput, roomInput, childSelect].forEach(el => {
        if (el) el.addEventListener('input', triggerBookingCalculation);
        if (el) el.addEventListener('change', triggerBookingCalculation);
    });
}

function triggerBookingCalculation() {
    const inDateInput = document.querySelector('input[type="date"][name="f5"]');
    const outDateInput = document.querySelector('input[type="date"][name="f6"]');
    const roomInput = document.querySelector('input[name="f4"]');
    const childSelect = document.querySelector('select[name="f8"]');
    const totalInput = document.querySelector('input[name="f10"]');

    if (!inDateInput || !outDateInput || !roomInput) return;

    const roomCode = roomInput.value.trim();
    const room = roomsMap[roomCode] || { price: 2200000 };
    const numChildren = parseInt(childSelect ? childSelect.value : 0, 10) || 0;

    const calc = calculateBookingPrice(room.price, inDateInput.value, outDateInput.value, 2, numChildren);

    if ($('#calcNights')) $('#calcNights').textContent = `${calc.nights} đêm`;
    if ($('#calcRoomPrice')) $('#calcRoomPrice').textContent = formatMoney(room.price);
    if ($('#calcChildSurcharge')) {
        if (calc.numChildren === 0) {
            $('#calcChildSurcharge').textContent = '0 ₫ (Không có trẻ em)';
        } else if (calc.numChildren === 1) {
            $('#calcChildSurcharge').textContent = '0 ₫ (Bé 1 <6t: Miễn phí)';
        } else {
            $('#calcChildSurcharge').textContent = `+${formatMoney(calc.childSurcharge)} (Phụ thu 5%/đêm cho bé 2)`;
        }
    }
    if ($('#calcTotal')) $('#calcTotal').textContent = formatMoney(calc.finalTotal);

    if (totalInput) totalInput.value = calc.finalTotal;
}

$('#recordForm').onsubmit = e => {
    e.preventDefault();
    const key = currentPage;
    const schema = schemas[key] || [];
    const values = schema.map((_, i) => new FormData(e.target).get('f' + i) || '');

    if (editingId) {
        const row = db[key].find(x => x.id === editingId);
        if (row) row.values = values;
        toast('Đã cập nhật dữ liệu thành công.');
    } else {
        const newRecord = { id: Date.now(), values };
        db[key].unshift(newRecord);
        toast('Đã thêm bản ghi mới thành công.');

        if (key === 'bookings' || key === 'dat_phong') {
            const roomCode = values[4];
            if (roomsMap[roomCode]) {
                roomsMap[roomCode].status = 'reserved';
            }

            // Gửi POST /api/bookings lên Java Spring Boot nếu có server
            fetch('/api/bookings', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    bookingId: values[0],
                    customerName: values[1],
                    phone: values[2],
                    building: values[3],
                    roomCode: values[4],
                    checkInDate: values[5],
                    checkOutDate: values[6],
                    adultsCount: Number(values[7]) || 2,
                    childrenUnder6Count: Number(values[8]) || 0,
                    status: values[9] || 'Chờ xác nhận'
                })
            }).catch(() => {});
        }
    }

    $('#modal').classList.remove('show');
    if (key === currentPage) {
        if (currentPage === 'rooms' || currentPage === 'phong') $('#pageContent').innerHTML = renderRoomPage();
        else renderTableRows();
    }
};

function editRecord(id) {
    openModal(currentPage, id);
}

function deleteRecord(id) {
    if (confirm('Bạn có chắc chắn muốn xóa bản ghi này?')) {
        db[currentPage] = db[currentPage].filter(r => r.id !== id);
        renderTableRows();
        toast('Đã xóa bản ghi.');
    }
}

$('#closeModal').onclick = $('#cancelModal').onclick = () => {
    $('#modal').classList.remove('show');
};

$('#modal').onclick = e => {
    if (e.target.id === 'modal') $('#modal').classList.remove('show');
};

function bookThisRoom(code) {
    const room = roomsMap[code];
    if (room && room.status !== 'available') {
        toast(`Phòng ${code} hiện đang ${room.status === 'occupied' ? 'có khách sử dụng' : 'bảo trì'}. Vui lòng chọn phòng trống khác.`);
        return;
    }
    navigate('dat_phong');
    openModal('dat_phong', null, {
        'Tòa nhà': room ? room.building : selectedBuilding,
        'Phòng': code
    });
}

// =============================================================================
// 12. ĐIỀU HƯỚNG TRANG CHÍNH (PAGE ROUTING)
// =============================================================================
// Các trang do Java (JSP) phụ trách: bấm menu sẽ chuyển sang trang JSP tương ứng (cùng giao diện)
const JSP_PAGES = {
    dat_phong: '/dat-phong', bookings: '/dat-phong',
    tien_nghi: '/tien-nghi', amenities: '/tien-nghi'
};
function jspUrl(path) {
    // Lấy context path từ địa chỉ hiện tại, ví dụ /The_Royal/index.html -> /The_Royal
    return location.pathname.replace(/\/[^\/]*$/, '') + path;
}

function navigate(key) {
    if (JSP_PAGES[key]) { location.href = jspUrl(JSP_PAGES[key]); return; }
    if (!pages[key]) return;
    currentPage = key;
    pageNum = 1;

    $$('.nav-item').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.page === key);
    });

    $('#crumb').textContent = pages[key][0];
    $('#pageTitle').textContent = pages[key][0];
    $('#pageDesc').textContent = pages[key][1];

    const isRoomPage = key === 'rooms' || key === 'phong';
    $('#headingAction').innerHTML = isRoomPage ? "<i class='bx bx-slider-alt'></i> Thiết lập phòng" : "<i class='bx bx-plus'></i> Tạo đặt phòng mới";
    $('#headingAction').onclick = () => {
        if (isRoomPage) openRoomSetupModal(selectedRoomCode);
        else {
            location.href = jspUrl('/dat-phong?action=timPhong');
        }
    };

    const contentEl = $('#pageContent');
    if (key === 'dashboard' || key === 'tong_quan') {
        contentEl.innerHTML = renderDashboard();
        drawRevenueChart();
    } else if (key === 'rooms' || key === 'phong') {
        contentEl.innerHTML = renderRoomPage();
    } else if (key === 'roomtypes' || key === 'loai_phong') {
        contentEl.innerHTML = renderRoomTypesPage();
    } else {
        contentEl.innerHTML = renderTablePage(key);
        renderTableRows();

        $('#tableSearch')?.addEventListener('input', () => {
            pageNum = 1;
            renderTableRows();
        });

        $('#tableFilter')?.addEventListener('change', () => {
            pageNum = 1;
            renderTableRows();
        });

        $('#addRecordBtn')?.addEventListener('click', () => {
            openModal(key);
        });
    }
}

// =============================================================================
// 13. THÔNG BÁO NHANH (TOAST NOTIFICATION)
// =============================================================================
function toast(msg) {
    const t = $('#toast');
    if (!t) return;
    t.innerHTML = `<i class='bx bx-check-circle' style='font-size:18px;'></i> <div>${msg}</div>`;
    t.classList.add('show');
    clearTimeout(window._toastTimeout);
    window._toastTimeout = setTimeout(() => {
        t.classList.remove('show');
    }, 2800);
}

// =============================================================================
// 14. KHỞI CHẠY ỨNG DỤNG VÀ LẮNG NGHE SỰ KIỆN TOÀN CỤC
// =============================================================================
$('#navigation').addEventListener('click', e => {
    const btn = e.target.closest('[data-page]');
    if (btn) {
        navigate(btn.dataset.page);
        $('#sidebar').classList.remove('open');
    }
});

$('#mobileMenu').onclick = () => {
    $('#sidebar').classList.toggle('open');
};

$('#today').textContent = new Intl.DateTimeFormat('vi-VN', {
    weekday: 'long',
    day: '2-digit',
    month: 'long',
    year: 'numeric'
}).format(new Date());

// Đồng bộ với Java Backend nếu có
syncWithJavaBackend();

// Khởi động trang mặc định Tổng quan
const startKey = location.hash.slice(1);
navigate(pages[startKey] && !JSP_PAGES[startKey] ? startKey : 'tong_quan');
