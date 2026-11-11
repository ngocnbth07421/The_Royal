-- =====================================================================
-- THE ROYAL HOTEL MANAGEMENT SYSTEM
-- Microsoft SQL Server 2016+ | T-SQL | ERD 27 bang
-- Full Script: DDL + Trigger + Index + Seed Data
-- Chay mot lan duy nhat tren database trong
-- =====================================================================

-- =====================================================================
-- PHAN 1: DDL — TAO 27 BANG
-- =====================================================================

CREATE DATABASE THE_ROYAL;
GO

USE THE_ROYAL;
GO

-- Bat dau transaction sau khi da chon database
BEGIN TRANSACTION;
-- ---------------------------------------------------------------------
-- NHOM TAI KHOAN: vai_tro, nhan_vien, khach_hang
-- ---------------------------------------------------------------------
CREATE TABLE vai_tro (
                         id_vai_tro  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                         ma_vai_tro  VARCHAR(30)   NOT NULL UNIQUE,
                         ten_vai_tro NVARCHAR(100) NOT NULL
);

CREATE TABLE nhan_vien (
                           id_nhan_vien  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                           id_vai_tro    BIGINT        NOT NULL REFERENCES vai_tro(id_vai_tro),
                           ma_nhan_vien  VARCHAR(20)   NOT NULL UNIQUE,
                           ho_ten        NVARCHAR(150) NOT NULL,
                           so_dien_thoai VARCHAR(20),
                           ngay_vao_lam  DATE,
                           luong_co_ban  NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (luong_co_ban >= 0),
                           phu_cap       NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (phu_cap >= 0),
                           khau_tru      NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (khau_tru >= 0),
                           trang_thai    VARCHAR(15)   NOT NULL DEFAULT 'HOAT_DONG'
                               CHECK (trang_thai IN ('HOAT_DONG','BI_KHOA'))
);

CREATE TABLE khach_hang (
                            id_khach_hang BIGINT        IDENTITY(1,1) PRIMARY KEY,
                            ho_ten        NVARCHAR(150) NOT NULL,
                            so_dien_thoai VARCHAR(20),
                            email         VARCHAR(150),
                            trang_thai    VARCHAR(15)   NOT NULL DEFAULT 'HOAT_DONG'
                                CHECK (trang_thai IN ('HOAT_DONG','BI_KHOA'))
);

-- ---------------------------------------------------------------------
-- NHOM DANH MUC PHONG
-- ---------------------------------------------------------------------
CREATE TABLE toa_nha (
                         id_toa_nha BIGINT        IDENTITY(1,1) PRIMARY KEY,
                         ma_toa_nha VARCHAR(5)    NOT NULL UNIQUE,
                         ten        NVARCHAR(100) NOT NULL
);

CREATE TABLE tang (
                      id_tang    BIGINT IDENTITY(1,1) PRIMARY KEY,
                      id_toa_nha BIGINT NOT NULL REFERENCES toa_nha(id_toa_nha),
                      so_tang    INT    NOT NULL CHECK (so_tang > 0),
                      UNIQUE (id_toa_nha, so_tang)
);

CREATE TABLE hang_phong (
                            id_hang_phong BIGINT        IDENTITY(1,1) PRIMARY KEY,
                            ma_hang_phong VARCHAR(20)   NOT NULL UNIQUE,
                            mo_ta         NVARCHAR(255)
);

CREATE TABLE loai_giuong (
                             id_loai_giuong BIGINT        IDENTITY(1,1) PRIMARY KEY,
                             ma_loai_giuong VARCHAR(20)   NOT NULL UNIQUE,
                             ten            NVARCHAR(100) NOT NULL,
                             suc_chua       INT           NOT NULL CHECK (suc_chua BETWEEN 1 AND 4)
);

CREATE TABLE loai_phong (
                            id_loai_phong  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                            id_hang_phong  BIGINT        NOT NULL REFERENCES hang_phong(id_hang_phong),
                            id_loai_giuong BIGINT        NOT NULL REFERENCES loai_giuong(id_loai_giuong),
                            ten            NVARCHAR(150) NOT NULL,
                            gia_moi_dem    NUMERIC(14,0) NOT NULL CHECK (gia_moi_dem > 0),
                            mo_ta          NVARCHAR(MAX),
                            UNIQUE (id_hang_phong, id_loai_giuong)
);

CREATE TABLE tien_nghi (
                           id_tien_nghi  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                           ten_tien_nghi NVARCHAR(100) NOT NULL UNIQUE,
                           so_luong      INT           NOT NULL DEFAULT 1 CHECK (so_luong >= 0)
);

CREATE TABLE phong (
                       id_phong       BIGINT        IDENTITY(1,1) PRIMARY KEY,
                       id_tang        BIGINT        NOT NULL REFERENCES tang(id_tang),
                       id_loai_phong  BIGINT        NOT NULL REFERENCES loai_phong(id_loai_phong),
                       ma_phong       VARCHAR(10)   NOT NULL UNIQUE,
                       hinh_anh_phong VARCHAR(500),
                       trang_thai     VARCHAR(15)   NOT NULL DEFAULT 'SAN_SANG'
                           CHECK (trang_thai IN ('SAN_SANG','DANG_O','CAN_DON','BAO_TRI')),
                       ghi_chu        NVARCHAR(255)
);

CREATE TABLE tien_nghi_phong (
                                 id_phong     BIGINT NOT NULL REFERENCES phong(id_phong) ON DELETE CASCADE,
                                 id_tien_nghi BIGINT NOT NULL REFERENCES tien_nghi(id_tien_nghi),
                                 PRIMARY KEY (id_phong, id_tien_nghi)
);

-- ---------------------------------------------------------------------
-- NHOM KHUYEN MAI VA THANH VIEN
-- ---------------------------------------------------------------------
CREATE TABLE ma_giam_gia (
                             id_ma_giam_gia  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                             ma_giam_gia     VARCHAR(30)   NOT NULL UNIQUE,
                             loai_giam_gia   VARCHAR(12)   NOT NULL CHECK (loai_giam_gia IN ('PHAN_TRAM','SO_TIEN')),
                             gia_tri         NUMERIC(14,2) NOT NULL CHECK (gia_tri > 0),
                             bat_dau_luc     DATETIME2     NOT NULL,
                             ket_thuc_luc    DATETIME2     NOT NULL,
                             so_luot_da_dung INT           NOT NULL DEFAULT 0 CHECK (so_luot_da_dung >= 0),
                             trang_thai      VARCHAR(12)   NOT NULL DEFAULT 'HOAT_DONG'
                                 CHECK (trang_thai IN ('HOAT_DONG','NGUNG')),
                             CHECK (ket_thuc_luc > bat_dau_luc)
);

CREATE TABLE hang_thanh_vien (
                                 id_hang_thanh_vien         BIGINT        IDENTITY(1,1) PRIMARY KEY,
                                 ma_hang_thanh_vien         VARCHAR(20)   NOT NULL UNIQUE,
                                 ten_hang_thanh_vien        NVARCHAR(50)  NOT NULL,
                                 so_dem_toi_thieu           INT           NOT NULL CHECK (so_dem_toi_thieu >= 0),
                                 muc_nap_toi_thieu          NUMERIC(14,0) NOT NULL CHECK (muc_nap_toi_thieu >= 0),
                                 phan_tram_giam_gia_phong   SMALLINT      NOT NULL CHECK (phan_tram_giam_gia_phong BETWEEN 0 AND 100),
                                 phan_tram_giam_gia_dich_vu SMALLINT      NOT NULL CHECK (phan_tram_giam_gia_dich_vu BETWEEN 0 AND 100)
);

CREATE TABLE the_thanh_vien (
                                id_the_thanh_vien  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                                id_khach_hang      BIGINT        NOT NULL UNIQUE REFERENCES khach_hang(id_khach_hang),
                                id_hang_thanh_vien BIGINT        NOT NULL REFERENCES hang_thanh_vien(id_hang_thanh_vien),
                                ma_qr              VARCHAR(64)   NOT NULL UNIQUE,
                                trang_thai         VARCHAR(12)   NOT NULL DEFAULT 'HOAT_DONG'
                                    CHECK (trang_thai IN ('HOAT_DONG','BI_KHOA','DA_DONG')),
                                so_du              NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (so_du >= 0),
                                kich_hoat_luc      DATETIME2     NOT NULL DEFAULT GETDATE(),
                                dong_the_luc       DATETIME2
);

-- ---------------------------------------------------------------------
-- NHOM DAT PHONG
-- ---------------------------------------------------------------------
CREATE TABLE dat_phong (
                           id_dat_phong         BIGINT        IDENTITY(1,1) PRIMARY KEY,
                           id_khach_hang        BIGINT        NOT NULL REFERENCES khach_hang(id_khach_hang),
                           id_nguoi_tao         BIGINT        REFERENCES nhan_vien(id_nhan_vien),
                           id_ma_giam_gia       BIGINT        REFERENCES ma_giam_gia(id_ma_giam_gia),
                           ma_dat_phong         VARCHAR(20)   NOT NULL UNIQUE,
                           trang_thai           VARCHAR(20)   NOT NULL DEFAULT 'CHO_COC'
                               CHECK (trang_thai IN ('CHO_COC','DA_XAC_NHAN','DANG_LUU_TRU',
                                                     'DA_TRA_PHONG','KHACH_HUY','KHACH_SAN_HUY','TU_HUY')),
                           thoi_gian_nhan_phong DATETIME2     NOT NULL,
                           thoi_gian_tra_phong  DATETIME2     NOT NULL,
                           so_dem               INT           NOT NULL CHECK (so_dem >= 1),
                           tien_phong           NUMERIC(14,0) NOT NULL DEFAULT 0,
                           giam_gia             NUMERIC(14,0) NOT NULL DEFAULT 0,
                           phi_dich_vu          NUMERIC(14,0) NOT NULL DEFAULT 0,
                           thue_vat             NUMERIC(14,0) NOT NULL DEFAULT 0,
                           tong_tien            NUMERIC(14,0) NOT NULL DEFAULT 0,
                           tien_coc             NUMERIC(14,0) NOT NULL DEFAULT 0,
                           tien_thanh_toan      NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (tien_thanh_toan >= 0),
                           CHECK (thoi_gian_tra_phong > thoi_gian_nhan_phong)
);

-- Ghi chu: SQL Server khong co kieu DATERANGE hay EXCLUDE USING GIST.
-- Thay the bang 2 cot ngay_nhan / ngay_tra va trigger kiem tra chong cheo.
CREATE TABLE dat_phong_chi_tiet (
                                    id_dat_phong_chi_tiet BIGINT   IDENTITY(1,1) PRIMARY KEY,
                                    id_dat_phong          BIGINT   NOT NULL REFERENCES dat_phong(id_dat_phong) ON DELETE CASCADE,
                                    id_phong              BIGINT   NOT NULL REFERENCES phong(id_phong),
                                    so_nguoi_lon          INT      NOT NULL DEFAULT 1 CHECK (so_nguoi_lon >= 1),
                                    so_tre_em_duoi_6      INT      NOT NULL DEFAULT 0 CHECK (so_tre_em_duoi_6 BETWEEN 0 AND 2),
                                    so_tre_em_tu_6_tuoi   INT      NOT NULL DEFAULT 0 CHECK (so_tre_em_tu_6_tuoi >= 0),
                                    trang_thai            VARCHAR(15) NOT NULL DEFAULT 'HIEU_LUC'
                                        CHECK (trang_thai IN ('HIEU_LUC','DANG_LUU_TRU','DA_TRA_PHONG','DA_HUY')),
                                    ngay_nhan             DATE     NOT NULL,  -- thay cho lower bound cua DATERANGE
                                    ngay_tra              DATE     NOT NULL,  -- thay cho upper bound cua DATERANGE
                                    CHECK (ngay_tra > ngay_nhan)
);

CREATE TABLE khach_luu_tru_chi_tiet (
                                        id_khach_luu_tru      BIGINT        IDENTITY(1,1) PRIMARY KEY,
                                        id_dat_phong_chi_tiet BIGINT        NOT NULL
                                            REFERENCES dat_phong_chi_tiet(id_dat_phong_chi_tiet) ON DELETE CASCADE,
                                        ho_ten                NVARCHAR(150) NOT NULL,
                                        ngay_sinh             DATE,
                                        quoc_tich             NVARCHAR(60),
                                        loai_giay_to          VARCHAR(10)   NOT NULL DEFAULT 'CCCD'
                                            CHECK (loai_giay_to IN ('CCCD','HO_CHIEU','KHAC','KHONG_CO')),
                                        so_giay_to            NVARCHAR(MAX),
                                        la_tre_em             BIT           NOT NULL DEFAULT 0
);

-- ---------------------------------------------------------------------
-- NHOM LUU TRU VA DICH VU
-- ---------------------------------------------------------------------
CREATE TABLE nhiem_vu_don_phong (
                                    id_nhiem_vu           BIGINT IDENTITY(1,1) PRIMARY KEY,
                                    id_phong              BIGINT NOT NULL REFERENCES phong(id_phong),
                                    id_dat_phong_chi_tiet BIGINT REFERENCES dat_phong_chi_tiet(id_dat_phong_chi_tiet),
                                    id_nhan_vien          BIGINT REFERENCES nhan_vien(id_nhan_vien),
                                    trang_thai            VARCHAR(12) NOT NULL DEFAULT 'CHO_XU_LY'
                                        CHECK (trang_thai IN ('CHO_XU_LY','DANG_LAM','HOAN_THANH'))
);

CREATE TABLE lich_su_doi_phong (
                                   id_lich_su_doi_phong  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                                   id_dat_phong_chi_tiet BIGINT        NOT NULL
                                       REFERENCES dat_phong_chi_tiet(id_dat_phong_chi_tiet),
                                   id_phong_cu           BIGINT        NOT NULL REFERENCES phong(id_phong),
                                   id_phong_moi          BIGINT        NOT NULL REFERENCES phong(id_phong),
                                   id_nguoi_tao          BIGINT        REFERENCES nhan_vien(id_nhan_vien),
                                   doi_luc               DATETIME2     NOT NULL DEFAULT GETDATE(),
                                   chenh_lech_gia        NUMERIC(14,0) NOT NULL DEFAULT 0,
                                   ly_do                 NVARCHAR(255),
                                   CHECK (id_phong_cu <> id_phong_moi)
);

CREATE TABLE dich_vu (
                         id_dich_vu  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                         ma_dich_vu  VARCHAR(20)   NOT NULL UNIQUE,
                         ten_dich_vu NVARCHAR(150) NOT NULL,
                         danh_muc    NVARCHAR(50),
                         don_vi_tinh NVARCHAR(30)  NOT NULL DEFAULT N'lan',
                         don_gia     NUMERIC(14,0) NOT NULL CHECK (don_gia >= 0),
                         trang_thai  VARCHAR(12)   NOT NULL DEFAULT 'HOAT_DONG'
                             CHECK (trang_thai IN ('HOAT_DONG','NGUNG'))
);

CREATE TABLE su_dung_dich_vu (
                                 id_su_dung_dich_vu BIGINT        IDENTITY(1,1) PRIMARY KEY,
                                 id_dat_phong       BIGINT        NOT NULL REFERENCES dat_phong(id_dat_phong),
                                 id_dich_vu         BIGINT        NOT NULL REFERENCES dich_vu(id_dich_vu),
                                 so_luong           NUMERIC(10,2) NOT NULL CHECK (so_luong > 0),
                                 thanh_tien         NUMERIC(14,0) NOT NULL CHECK (thanh_tien >= 0),
                                 tien_giam_gia      NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (tien_giam_gia >= 0),
                                 thoi_gian_su_dung  DATETIME2     NOT NULL DEFAULT GETDATE()
);

-- ---------------------------------------------------------------------
-- NHOM HUY, THANH TOAN, DANH GIA
-- ---------------------------------------------------------------------
CREATE TABLE lich_su_dat_phong (
                                   id_lich_su_dat_phong BIGINT        IDENTITY(1,1) PRIMARY KEY,
                                   id_dat_phong         BIGINT        NOT NULL UNIQUE REFERENCES dat_phong(id_dat_phong),
                                   id_nguoi_huy         BIGINT        REFERENCES nhan_vien(id_nhan_vien),
                                   id_ma_giam_gia       BIGINT        REFERENCES ma_giam_gia(id_ma_giam_gia),
                                   so_tien_hoan         NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (so_tien_hoan >= 0),
                                   ghi_chu              NVARCHAR(255)
);

CREATE TABLE thanh_toan (
                            id_thanh_toan          BIGINT        IDENTITY(1,1) PRIMARY KEY,
                            id_dat_phong           BIGINT        NOT NULL REFERENCES dat_phong(id_dat_phong),
                            id_the_thanh_vien      BIGINT        REFERENCES the_thanh_vien(id_the_thanh_vien),
                            phuong_thuc_thanh_toan VARCHAR(10)   NOT NULL
                                CHECK (phuong_thuc_thanh_toan IN ('TIEN_MAT','VNPAY','THE')),
                            tien_thanh_toan        NUMERIC(14,0) NOT NULL CHECK (tien_thanh_toan > 0),
                            trang_thai             VARCHAR(12)   NOT NULL DEFAULT 'CHO_XU_LY'
                                CHECK (trang_thai IN ('CHO_XU_LY','THANH_CONG','THAT_BAI','DA_HOAN')),
    -- Neu phuong_thuc = 'THE' thi bat buoc phai co id_the_thanh_vien
                            CHECK (phuong_thuc_thanh_toan <> 'THE' OR id_the_thanh_vien IS NOT NULL)
);

CREATE TABLE danh_gia (
                          id_danh_gia   BIGINT   IDENTITY(1,1) PRIMARY KEY,
                          id_dat_phong  BIGINT   NOT NULL UNIQUE REFERENCES dat_phong(id_dat_phong),
                          id_khach_hang BIGINT   NOT NULL REFERENCES khach_hang(id_khach_hang),
                          diem_danh_gia SMALLINT NOT NULL CHECK (diem_danh_gia BETWEEN 1 AND 5),
                          binh_luan     NVARCHAR(MAX),
                          bi_an         BIT      NOT NULL DEFAULT 0,
                          ly_do_an      NVARCHAR(255)
);

-- ---------------------------------------------------------------------
-- NHOM NHAN SU VA THU CHI
-- ---------------------------------------------------------------------
CREATE TABLE bang_luong (
                            id_bang_luong  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                            id_nhan_vien   BIGINT        NOT NULL REFERENCES nhan_vien(id_nhan_vien),
                            thoi_gian_nhan DATE          NOT NULL,
                            luong_co_ban   NUMERIC(14,0) NOT NULL,
                            phu_cap        NUMERIC(14,0) NOT NULL DEFAULT 0,
                            khau_tru       NUMERIC(14,0) NOT NULL DEFAULT 0,
                            luong_tong     NUMERIC(14,0) NOT NULL,
                            trang_thai     VARCHAR(10)   NOT NULL DEFAULT 'NHAP'
                                CHECK (trang_thai IN ('NHAP','DA_CHOT')),
                            CHECK (luong_tong = luong_co_ban + phu_cap - khau_tru)
);

CREATE TABLE danh_muc_chi (
                              id_danh_muc  BIGINT        IDENTITY(1,1) PRIMARY KEY,
                              ten_danh_muc NVARCHAR(100) NOT NULL UNIQUE,
                              loai_chi_phi VARCHAR(12)   NOT NULL
                                  CHECK (loai_chi_phi IN ('LUONG','TIEN_ICH','BAO_TRI','VAT_TU','KHAC')),
                              so_tien      NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (so_tien >= 0)
);

CREATE TABLE bang_thu_chi (
                              id_thu_chi    BIGINT        IDENTITY(1,1) PRIMARY KEY,
                              id_danh_muc   BIGINT        NOT NULL REFERENCES danh_muc_chi(id_danh_muc),
                              id_bang_luong BIGINT        REFERENCES bang_luong(id_bang_luong),
                              id_nguoi_tao  BIGINT        REFERENCES nhan_vien(id_nhan_vien),
                              thoi_gian     DATETIME2     NOT NULL DEFAULT GETDATE(),
                              doanh_thu     NUMERIC(14,0) NOT NULL DEFAULT 0 CHECK (doanh_thu >= 0),
                              ghi_chu       NVARCHAR(255)
);

-- =====================================================================
-- PHAN 2: TRIGGER
-- =====================================================================

-- ---------------------------------------------------------------------
-- Trigger 1: Kiem tra suc chua phong (thay cho plpgsql function)
-- ---------------------------------------------------------------------
GO
CREATE TRIGGER trg_dat_phong_chi_tiet_suc_chua
    ON dat_phong_chi_tiet
    AFTER INSERT, UPDATE
                      AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (
        SELECT 1
        FROM inserted i
        JOIN phong       p  ON p.id_phong       = i.id_phong
        JOIN loai_phong  lp ON lp.id_loai_phong  = p.id_loai_phong
        JOIN loai_giuong lg ON lg.id_loai_giuong = lp.id_loai_giuong
        WHERE i.so_nguoi_lon + i.so_tre_em_tu_6_tuoi > lg.suc_chua
    )
BEGIN
ROLLBACK TRANSACTION;
RAISERROR('So nguoi vuot suc chua cua phong.', 16, 1);
        RETURN;
END
END;
GO

-- ---------------------------------------------------------------------
-- Trigger 2: Kiem tra chong cheo ngay dat phong
--            (thay cho EXCLUDE USING gist)
-- ---------------------------------------------------------------------
CREATE TRIGGER trg_dat_phong_chi_tiet_overlap
    ON dat_phong_chi_tiet
    AFTER INSERT, UPDATE
                      AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (
        SELECT 1
        FROM inserted i
        JOIN dat_phong_chi_tiet d
            ON  d.id_phong              = i.id_phong
            AND d.id_dat_phong_chi_tiet <> i.id_dat_phong_chi_tiet
            AND d.trang_thai IN ('HIEU_LUC','DANG_LUU_TRU')
            AND i.trang_thai IN ('HIEU_LUC','DANG_LUU_TRU')
            AND d.ngay_nhan < i.ngay_tra   -- khoang [nhan, tra) giao nhau
            AND d.ngay_tra  > i.ngay_nhan
    )
BEGIN
ROLLBACK TRANSACTION;
RAISERROR('Phong da duoc dat trong khoang thoi gian nay. Vui long chon phong khac.', 16, 1);
        RETURN;
END
END;
GO

-- =====================================================================
-- PHAN 3: CHI MUC (INDEX)
-- =====================================================================
CREATE INDEX ix_phong_loai_trang_thai  ON phong(id_loai_phong, trang_thai);
CREATE INDEX ix_dat_phong_khach_hang   ON dat_phong(id_khach_hang);
CREATE INDEX ix_dat_phong_trang_thai   ON dat_phong(trang_thai, thoi_gian_nhan_phong);
CREATE INDEX ix_dat_phong_chi_tiet_dat ON dat_phong_chi_tiet(id_dat_phong);
CREATE INDEX ix_thanh_toan_dat_phong   ON thanh_toan(id_dat_phong);
CREATE INDEX ix_su_dung_dich_vu_dat    ON su_dung_dich_vu(id_dat_phong);
CREATE INDEX ix_khach_luu_tru          ON khach_luu_tru_chi_tiet(id_dat_phong_chi_tiet);
CREATE INDEX ix_nhiem_vu_trang_thai    ON nhiem_vu_don_phong(trang_thai, id_nhan_vien);

-- =====================================================================
-- PHAN 4: SEED DATA — DU LIEU MAU
-- =====================================================================

-- ---------------------------------------------------------------------
-- BANG LOOKUP / THAM CHIEU
-- ---------------------------------------------------------------------
INSERT INTO vai_tro(ma_vai_tro, ten_vai_tro) VALUES
                                                 ('KHACH_HANG', N'Khach hang'),
                                                 ('LE_TAN',     N'Le tan'),
                                                 ('BUONG_PHONG',N'Buong phong'),
                                                 ('KE_TOAN',    N'Ke toan'),
                                                 ('QUAN_LY',    N'Quan ly'),
                                                 ('ADMIN',      N'Admin');

INSERT INTO hang_thanh_vien(ma_hang_thanh_vien, ten_hang_thanh_vien,
                            so_dem_toi_thieu, muc_nap_toi_thieu,
                            phan_tram_giam_gia_phong, phan_tram_giam_gia_dich_vu) VALUES
                                                                                      ('HOI_VIEN',  N'Hoi vien',    1,  10000000,  0,  5),
                                                                                      ('BAC',       N'Bac',        10,  60000000,  5, 10),
                                                                                      ('VANG',      N'Vang',       25, 130000000, 20, 25),
                                                                                      ('KIM_CUONG', N'Kim Cuong',  50, 300000000, 45, 50);

INSERT INTO hang_phong(ma_hang_phong, mo_ta) VALUES
                                                 ('STANDARD',    N'Standard Room'),
                                                 ('DELUXE',      N'Deluxe Room'),
                                                 ('SUITE',       N'Suite Room'),
                                                 ('PRESIDENTIAL',N'Presidential Suite');

INSERT INTO loai_giuong(ma_loai_giuong, ten, suc_chua) VALUES
                                                           ('SINGLE', N'Single Room', 1),
                                                           ('TWIN',   N'Twin Room',   2),
                                                           ('DOUBLE', N'Double Room', 2),
                                                           ('TRIPLE', N'Triple Room', 3),
                                                           ('FAMILY', N'Family Room', 4);

-- 13 loai phong hop le; gia tinh theo hang phong * he so loai giuong
-- SQL Server: dung EXISTS + VALUES thay cho tuple IN
INSERT INTO loai_phong(id_hang_phong, id_loai_giuong, ten, gia_moi_dem)
SELECT h.id_hang_phong,
       g.id_loai_giuong,
       h.mo_ta + N' - ' + g.ten,
       CAST(ROUND(
               CAST(CASE h.ma_hang_phong
                        WHEN 'STANDARD'     THEN 1200000.0
                        WHEN 'DELUXE'       THEN 2000000.0
                        WHEN 'SUITE'        THEN 3500000.0
                        ELSE                     12000000.0
                        END
                   * CASE g.ma_loai_giuong
                         WHEN 'SINGLE' THEN 0.8
                         WHEN 'TRIPLE' THEN 1.25
                         WHEN 'FAMILY' THEN 1.5
                         ELSE               1.0
                        END
                   AS FLOAT), -3) AS BIGINT)
FROM hang_phong h
         JOIN loai_giuong g ON EXISTS (
    SELECT 1 FROM (VALUES
                       ('STANDARD','SINGLE'),('STANDARD','TWIN'),('STANDARD','DOUBLE'),('STANDARD','TRIPLE'),
                       ('DELUXE','SINGLE'),  ('DELUXE','TWIN'),  ('DELUXE','DOUBLE'),  ('DELUXE','TRIPLE'),
                       ('SUITE','DOUBLE'),   ('SUITE','TRIPLE'),  ('SUITE','FAMILY'),
                       ('PRESIDENTIAL','DOUBLE'), ('PRESIDENTIAL','FAMILY')
                  ) AS v(mh, mg)
    WHERE v.mh = h.ma_hang_phong AND v.mg = g.ma_loai_giuong
)
ORDER BY h.id_hang_phong, g.id_loai_giuong;

INSERT INTO toa_nha(ma_toa_nha, ten) VALUES
                                         ('C', N'Clarence'),
                                         ('W', N'Windsor');

-- Tang 1-30 cho moi toa nha (thay cho generate_series bang recursive CTE)
WITH nums AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM nums WHERE n < 30
)
INSERT INTO tang(id_toa_nha, so_tang)
SELECT t.id_toa_nha, nums.n
FROM toa_nha t CROSS JOIN nums
    OPTION (MAXRECURSION 30);

-- 300 phong: tang 1-12 Standard, 13-24 Deluxe, 25-29 Suite, 30 Presidential
-- 5 phong moi tang, kieu giuong xoay vong trong moi hang
-- SQL Server: dung 2 recursive CTE + ROW_NUMBER / modulo
WITH
    nums5 AS (
        SELECT 1 AS n UNION ALL SELECT n+1 FROM nums5 WHERE n < 5
    ),
    loai AS (
        SELECT l.id_loai_phong,
               h.ma_hang_phong,
               CAST(ROW_NUMBER() OVER (PARTITION BY h.ma_hang_phong ORDER BY l.id_loai_phong) - 1 AS INT) AS stt,
               CAST(COUNT(*)     OVER (PARTITION BY h.ma_hang_phong)                           AS INT) AS tong
        FROM loai_phong l
                 JOIN hang_phong h ON h.id_hang_phong = l.id_hang_phong
    ),
    vi_tri AS (
        SELECT t.id_tang,
               tn.ma_toa_nha AS ma_toa,
               t.so_tang,
               n5.n,
               CASE WHEN t.so_tang <= 12 THEN 'STANDARD'
                    WHEN t.so_tang <= 24 THEN 'DELUXE'
                    WHEN t.so_tang <= 29 THEN 'SUITE'
                    ELSE 'PRESIDENTIAL' END AS ma_hang
        FROM tang t
                 JOIN toa_nha tn ON tn.id_toa_nha = t.id_toa_nha
                 CROSS JOIN nums5 n5
    )
INSERT INTO phong(ma_phong, id_tang, id_loai_phong)
SELECT  v.ma_toa
            + RIGHT('0' + CAST(v.so_tang AS VARCHAR(2)), 2)
    + RIGHT('0' + CAST(v.n       AS VARCHAR(2)), 2),
    v.id_tang,
    l.id_loai_phong
FROM vi_tri v
    JOIN loai l ON l.ma_hang_phong = v.ma_hang
    AND l.stt = (v.so_tang + v.n) % l.tong
OPTION (MAXRECURSION 5);

INSERT INTO tien_nghi(ten_tien_nghi) VALUES
                                         (N'Wifi toc do cao'),
                                         (N'Dieu hoa'),
                                         (N'Tivi man hinh phang'),
                                         (N'Minibar'),
                                         (N'Ket an toan'),
                                         (N'Bon tam'),
                                         (N'Ban lam viec'),
                                         (N'May pha ca phe');

INSERT INTO dich_vu(ma_dich_vu, ten_dich_vu, danh_muc, don_vi_tinh, don_gia) VALUES
                                                                                 ('DV_GIAT_UI',    N'Giat ui',           N'Tien ich',  N'kg',     60000),
                                                                                 ('DV_AN_SANG',    N'An sang tai phong', N'An uong',   N'suat',  180000),
                                                                                 ('DV_MINIBAR',    N'Do uong minibar',   N'An uong',   N'lan',    50000),
                                                                                 ('DV_SPA',        N'Spa 60 phut',       N'Thu gian',  N'luot',  650000),
                                                                                 ('DV_DON_SAN_BAY',N'Don san bay',       N'Di chuyen', N'luot',  450000);

INSERT INTO danh_muc_chi(ten_danh_muc, loai_chi_phi) VALUES
                                                         (N'Luong nhan vien',       'LUONG'),
                                                         (N'Dien',                  'TIEN_ICH'),
                                                         (N'Nuoc',                  'TIEN_ICH'),
                                                         (N'Bao tri, sua chua',     'BAO_TRI'),
                                                         (N'Vat tu, do dung phong', 'VAT_TU'),
                                                         (N'Chi phi khac',          'KHAC');

-- ---------------------------------------------------------------------
-- LOP 1: nhan_vien (10), khach_hang (10), ma_giam_gia (10)
-- vai_tro: 1=KHACH_HANG 2=LE_TAN 3=BUONG_PHONG 4=KE_TOAN 5=QUAN_LY 6=ADMIN
-- ---------------------------------------------------------------------
INSERT INTO nhan_vien(id_vai_tro, ma_nhan_vien, ho_ten, so_dien_thoai,
                      ngay_vao_lam, luong_co_ban, phu_cap, khau_tru, trang_thai) VALUES
                                                                                     (6, 'NV001', N'Nguyen Minh Duc',  '0901111001', '2021-03-01', 25000000, 3000000, 500000, 'HOAT_DONG'),
                                                                                     (5, 'NV002', N'Tran Thi Lan Anh', '0901111002', '2021-05-15', 20000000, 2500000, 400000, 'HOAT_DONG'),
                                                                                     (5, 'NV003', N'Le Van Thanh',     '0901111003', '2022-01-10', 20000000, 2500000, 400000, 'HOAT_DONG'),
                                                                                     (2, 'NV004', N'Pham Thi Huong',   '0901111004', '2022-04-01', 12000000, 1500000, 200000, 'HOAT_DONG'),
                                                                                     (2, 'NV005', N'Vo Thi Mai',       '0901111005', '2022-07-20', 12000000, 1500000, 200000, 'HOAT_DONG'),
                                                                                     (2, 'NV006', N'Nguyen Duc Huy',   '0901111006', '2023-02-01', 12000000, 1500000, 200000, 'HOAT_DONG'),
                                                                                     (3, 'NV007', N'Bui Thi Thu',      '0901111007', '2022-09-01', 10000000, 1000000, 150000, 'HOAT_DONG'),
                                                                                     (3, 'NV008', N'Dang Van Long',    '0901111008', '2023-01-15', 10000000, 1000000, 150000, 'HOAT_DONG'),
                                                                                     (4, 'NV009', N'Ho Thi Bich',      '0901111009', '2021-11-01', 15000000, 2000000, 300000, 'HOAT_DONG'),
                                                                                     (5, 'NV010', N'Truong Van Khanh', '0901111010', '2024-03-01', 20000000, 2500000, 400000, 'BI_KHOA');

INSERT INTO khach_hang(ho_ten, so_dien_thoai, email, trang_thai) VALUES
                                                                     (N'Nguyen Van An',      '0912001001', 'an.nguyen@gmail.com',   'HOAT_DONG'),
                                                                     (N'Tran Thi Bich Ngoc', '0912001002', 'ngoc.tran@gmail.com',   'HOAT_DONG'),
                                                                     (N'Le Minh Hoang',      '0912001003', 'hoang.le@gmail.com',    'HOAT_DONG'),
                                                                     (N'Pham Thi Cam',       '0912001004', 'cam.pham@gmail.com',    'HOAT_DONG'),
                                                                     (N'Vo Duc Khoa',        '0912001005', 'khoa.vo@gmail.com',     'HOAT_DONG'),
                                                                     (N'Dang Thi Linh',      '0912001006', 'linh.dang@gmail.com',   'HOAT_DONG'),
                                                                     (N'Bui Van Manh',       '0912001007', 'manh.bui@gmail.com',    'HOAT_DONG'),
                                                                     (N'Ho Thi Nhu',         '0912001008', 'nhu.ho@gmail.com',      'HOAT_DONG'),
                                                                     (N'Truong Minh Quan',   '0912001009', 'quan.truong@gmail.com', 'HOAT_DONG'),
                                                                     (N'Ly Thi Suong',       '0912001010', 'suong.ly@gmail.com',    'BI_KHOA');

INSERT INTO ma_giam_gia(ma_giam_gia, loai_giam_gia, gia_tri,
                        bat_dau_luc, ket_thuc_luc, so_luot_da_dung, trang_thai) VALUES
                                                                                    ('WELCOME10', 'PHAN_TRAM',      10, '2026-01-01 00:00:00', '2026-12-31 23:59:59',  5, 'HOAT_DONG'),
                                                                                    ('SUMMER20',  'PHAN_TRAM',      20, '2026-06-01 00:00:00', '2026-08-31 23:59:59',  3, 'HOAT_DONG'),
                                                                                    ('LOYAL500K', 'SO_TIEN',    500000, '2026-01-01 00:00:00', '2026-12-31 23:59:59',  8, 'HOAT_DONG'),
                                                                                    ('NEWYR25',   'PHAN_TRAM',      25, '2026-01-01 00:00:00', '2026-01-07 23:59:59', 10, 'NGUNG'),
                                                                                    ('FLASH15',   'PHAN_TRAM',      15, '2026-11-11 00:00:00', '2026-11-15 23:59:59',  0, 'HOAT_DONG'),
                                                                                    ('VIP1M',     'SO_TIEN',   1000000, '2026-01-01 00:00:00', '2026-12-31 23:59:59',  2, 'HOAT_DONG'),
                                                                                    ('BDAY30',    'PHAN_TRAM',      30, '2026-01-01 00:00:00', '2026-12-31 23:59:59',  1, 'HOAT_DONG'),
                                                                                    ('CORP10',    'PHAN_TRAM',      10, '2026-04-01 00:00:00', '2026-09-30 23:59:59',  6, 'NGUNG'),
                                                                                    ('EARLYBIRD', 'SO_TIEN',    200000, '2026-09-01 00:00:00', '2026-12-31 23:59:59',  4, 'HOAT_DONG'),
                                                                                    ('WEEKEND12', 'PHAN_TRAM',      12, '2026-01-01 00:00:00', '2026-12-31 23:59:59',  7, 'HOAT_DONG');

-- ---------------------------------------------------------------------
-- LOP 2: the_thanh_vien (6), tien_nghi_phong (10)
-- id_hang_thanh_vien: 1=HOI_VIEN 2=BAC 3=VANG 4=KIM_CUONG
-- ---------------------------------------------------------------------
INSERT INTO the_thanh_vien(id_khach_hang, id_hang_thanh_vien,
                           ma_qr, trang_thai, so_du, kich_hoat_luc) VALUES
                                                                        (1, 2, 'QR-KH001-BAC-2024',  'HOAT_DONG',  85000000, '2024-03-15 09:00:00'),
                                                                        (2, 3, 'QR-KH002-VANG-2023', 'HOAT_DONG', 160000000, '2023-08-20 10:00:00'),
                                                                        (3, 1, 'QR-KH003-HOI-2025',  'HOAT_DONG',  12000000, '2025-01-10 08:30:00'),
                                                                        (4, 4, 'QR-KH004-KIM-2022',  'HOAT_DONG', 450000000, '2022-05-01 11:00:00'),
                                                                        (5, 2, 'QR-KH005-BAC-2024',  'HOAT_DONG',  70000000, '2024-07-22 09:00:00'),
                                                                        (6, 1, 'QR-KH006-HOI-2026',  'HOAT_DONG',  15000000, '2026-02-14 14:00:00');

INSERT INTO tien_nghi_phong(id_phong, id_tien_nghi)
SELECT p.id_phong, t.id_tien_nghi
FROM (VALUES
          ('C0101',1),('C0101',2),('C0101',3),
          ('C0501',1),('C0501',2),('C0501',4),
          ('W1301',1),('W1301',2),('W1301',6),
          ('C3001',7)
     ) AS d(ma_phong, id_tien_nghi)
         JOIN phong     p ON p.ma_phong    = d.ma_phong
         JOIN tien_nghi t ON t.id_tien_nghi = d.id_tien_nghi;

-- ---------------------------------------------------------------------
-- LOP 3: dat_phong (10)
-- id_khach_hang (1-10), id_nguoi_tao (1-9, NULL=khach tu dat)
-- id_ma_giam_gia (1-10, NULL=khong dung ma)
-- ---------------------------------------------------------------------
INSERT INTO dat_phong(id_khach_hang, id_nguoi_tao, id_ma_giam_gia, ma_dat_phong,
                      trang_thai, thoi_gian_nhan_phong, thoi_gian_tra_phong, so_dem,
                      tien_phong, giam_gia, phi_dich_vu, thue_vat, tong_tien, tien_coc, tien_thanh_toan)
VALUES
-- DP001: Khach 1, 2 dem, Standard Double, WELCOME10 (-10%)
(1, 4, 1, 'DP2026001','DA_TRA_PHONG',
 '2026-10-01 14:00:00','2026-10-03 12:00:00',2,
 2400000,240000,180000,233600,2573600,500000,2573600),
-- DP002: Khach 2 (the VANG -20% phong), 3 dem, Deluxe Twin
(2, 5, NULL,'DP2026002','DANG_LUU_TRU',
 '2026-11-10 14:00:00','2026-11-13 12:00:00',3,
 4800000,960000,650000,429600,4919600,1000000,0),
-- DP003: Khach 3, 1 dem, Suite Family, FLASH15 (-15%)
(3, 4, 5,'DP2026003','DA_XAC_NHAN',
 '2026-11-15 14:00:00','2026-11-16 12:00:00',1,
 5250000,787500,0,444600,4907100,1000000,0),
-- DP004: Khach 4 (the KIM_CUONG -45% phong), 5 dem, Presidential Double
(4, NULL, NULL,'DP2026004','DA_TRA_PHONG',
 '2026-09-15 14:00:00','2026-09-20 12:00:00',5,
 60000000,27000000,2250000,3145000,38395000,10000000,38395000),
-- DP005: Khach 5, 2 dem, Deluxe Single, cho coc
(5, 6, NULL,'DP2026005','CHO_COC',
 '2026-11-20 14:00:00','2026-11-22 12:00:00',2,
 2240000,0,0,201600,2441600,500000,0),
-- DP006: Khach 6, 2 dem, Standard Twin, LOYAL500K (-500k)
(6, 5, 3,'DP2026006','DA_TRA_PHONG',
 '2026-10-20 14:00:00','2026-10-22 12:00:00',2,
 2400000,500000,60000,175600,2135600,500000,2135600),
-- DP007: Khach 7, 4 dem, Deluxe Triple, dang luu tru
(7, 4, NULL,'DP2026007','DANG_LUU_TRU',
 '2026-11-08 14:00:00','2026-11-12 12:00:00',4,
 10000000,0,450000,940500,11390500,2000000,0),
-- DP008: Khach 8, 1 dem, Standard Single, khach huy
(8, 5, NULL,'DP2026008','KHACH_HUY',
 '2026-11-05 14:00:00','2026-11-06 12:00:00',1,
 960000,0,0,86400,1046400,200000,0),
-- DP009: Khach 9, 3 dem, Suite Triple, EARLYBIRD (-200k)
(9, 6, 9,'DP2026009','DA_XAC_NHAN',
 '2026-12-24 14:00:00','2026-12-27 12:00:00',3,
 13125000,200000,0,1157700,14082700,3000000,0),
-- DP010: Khach 1, 2 dem, Deluxe Double, WEEKEND12 (-12%)
(1, 4, 10,'DP2026010','DA_TRA_PHONG',
 '2026-11-01 14:00:00','2026-11-03 12:00:00',2,
 4000000,480000,180000,322000,4022000,1000000,4022000);

-- ---------------------------------------------------------------------
-- LOP 4: dat_phong_chi_tiet (10)
-- ngay_nhan / ngay_tra thay cho DATERANGE '[nhan, tra)'
-- ---------------------------------------------------------------------
INSERT INTO dat_phong_chi_tiet(id_dat_phong, id_phong,
                               so_nguoi_lon, so_tre_em_duoi_6, so_tre_em_tu_6_tuoi,
                               trang_thai, ngay_nhan, ngay_tra)
SELECT dp.id_dat_phong, p.id_phong,
       d.sl, d.te_duoi6, d.te_tu6,
       d.tt, CAST(d.ngay_nhan AS DATE), CAST(d.ngay_tra AS DATE)
FROM (VALUES
          ('DP2026001','C0301',1,0,0,'DA_TRA_PHONG','2026-10-01','2026-10-03'),
          ('DP2026002','W1302',2,0,0,'DANG_LUU_TRU','2026-11-10','2026-11-13'),
          ('DP2026003','C2501',2,1,0,'HIEU_LUC',    '2026-11-15','2026-11-16'),
          ('DP2026004','C3001',2,0,0,'DA_TRA_PHONG','2026-09-15','2026-09-20'),
          ('DP2026005','W1301',1,0,0,'HIEU_LUC',    '2026-11-20','2026-11-22'),
          ('DP2026006','C0204',2,0,0,'DA_TRA_PHONG','2026-10-20','2026-10-22'),
          ('DP2026007','W1401',3,0,0,'DANG_LUU_TRU','2026-11-08','2026-11-12'),
          ('DP2026008','C0103',1,0,0,'DA_HUY',      '2026-11-05','2026-11-06'),
          ('DP2026009','W2503',2,0,1,'HIEU_LUC',    '2026-12-24','2026-12-27'),
          ('DP2026010','C1301',2,0,0,'DA_TRA_PHONG','2026-11-01','2026-11-03')
     ) AS d(ma_dp, ma_phong, sl, te_duoi6, te_tu6, tt, ngay_nhan, ngay_tra)
         JOIN dat_phong dp ON dp.ma_dat_phong = d.ma_dp
         JOIN phong     p  ON p.ma_phong      = d.ma_phong;

-- ---------------------------------------------------------------------
-- LOP 5a: khach_luu_tru_chi_tiet (10)
-- BIT thay cho BOOLEAN; 1/0 thay cho TRUE/FALSE
-- ---------------------------------------------------------------------
INSERT INTO khach_luu_tru_chi_tiet(id_dat_phong_chi_tiet,
                                   ho_ten, ngay_sinh, quoc_tich, loai_giay_to, so_giay_to, la_tre_em)
SELECT dpct.id_dat_phong_chi_tiet,
       d.ho_ten, CAST(d.ngay_sinh AS DATE), d.quoc_tich,
       d.loai_giay_to, d.so_giay_to, d.la_tre_em
FROM (VALUES
          ('DP2026001',N'Nguyen Van An',      '1990-05-15',N'Viet Nam','CCCD',    '001090012345',0),
          ('DP2026002',N'Tran Thi Bich Ngoc', '1988-09-20',N'Viet Nam','CCCD',    '001088098765',0),
          ('DP2026002',N'Tran Van Binh',      '1986-03-10',N'Viet Nam','CCCD',    '001086031234',0),
          ('DP2026003',N'Le Minh Hoang',      '1995-07-25',N'Viet Nam','CCCD',    '001095074321',0),
          ('DP2026003',N'Le Thi Hoa',         '2022-01-10',N'Viet Nam','KHONG_CO',NULL,           1),
          ('DP2026004',N'Pham Thi Cam',       '1980-12-01',N'Viet Nam','HO_CHIEU','P123456789',  0),
          ('DP2026006',N'Dang Thi Linh',      '1992-04-18',N'Viet Nam','CCCD',    '001092044567',0),
          ('DP2026007',N'Bui Van Manh',       '1985-11-30',N'Viet Nam','CCCD',    '001085118901',0),
          ('DP2026009',N'Truong Minh Quan',   '1991-08-14',N'Viet Nam','CCCD',    '001091082233',0),
          ('DP2026010',N'Nguyen Van An',      '1990-05-15',N'Viet Nam','CCCD',    '001090012345',0)
     ) AS d(ma_dp, ho_ten, ngay_sinh, quoc_tich, loai_giay_to, so_giay_to, la_tre_em)
         JOIN dat_phong          dp   ON dp.ma_dat_phong   = d.ma_dp
         JOIN dat_phong_chi_tiet dpct ON dpct.id_dat_phong = dp.id_dat_phong;

-- ---------------------------------------------------------------------
-- LOP 5b: nhiem_vu_don_phong (10)
-- ---------------------------------------------------------------------
INSERT INTO nhiem_vu_don_phong(id_phong, id_dat_phong_chi_tiet, id_nhan_vien, trang_thai)
SELECT p.id_phong, dpct.id_dat_phong_chi_tiet, nv.id_nhan_vien, d.tt
FROM (VALUES
          ('C0301','DP2026001','NV007','HOAN_THANH'),
          ('W1302','DP2026002','NV008','DANG_LAM'),
          ('C2501','DP2026003','NV007','CHO_XU_LY'),
          ('C3001','DP2026004','NV007','HOAN_THANH'),
          ('W1301','DP2026005','NV008','CHO_XU_LY'),
          ('C0204','DP2026006','NV008','HOAN_THANH'),
          ('W1401','DP2026007','NV007','DANG_LAM'),
          ('C0103','DP2026008','NV007','HOAN_THANH'),
          ('W2503','DP2026009','NV008','CHO_XU_LY'),
          ('C1301','DP2026010','NV007','HOAN_THANH')
     ) AS d(ma_phong, ma_dp, ma_nv, tt)
         JOIN phong              p    ON p.ma_phong         = d.ma_phong
         JOIN dat_phong          dp   ON dp.ma_dat_phong    = d.ma_dp
         JOIN dat_phong_chi_tiet dpct ON dpct.id_dat_phong  = dp.id_dat_phong
         JOIN nhan_vien          nv   ON nv.ma_nhan_vien    = d.ma_nv;

-- ---------------------------------------------------------------------
-- LOP 5c: lich_su_doi_phong (3)
-- ---------------------------------------------------------------------
INSERT INTO lich_su_doi_phong(id_dat_phong_chi_tiet,
                              id_phong_cu, id_phong_moi, id_nguoi_tao,
                              doi_luc, chenh_lech_gia, ly_do)
SELECT dpct.id_dat_phong_chi_tiet,
       pc.id_phong, pm.id_phong, nv.id_nhan_vien,
       CAST(d.doi_luc AS DATETIME2),
       CAST(d.chenh_lech AS NUMERIC(14,0)),
       d.ly_do
FROM (VALUES
          ('DP2026007','W1401','W1405','NV004','2026-11-09 10:00:00',       0,N'Khach yeu cau phong yen tinh hon'),
          ('DP2026001','C0301','C0302','NV005','2026-10-01 15:30:00',       0,N'Phong C0301 co su co dieu hoa'),
          ('DP2026004','C3001','W3005','NV004','2026-09-16 09:00:00',30000000,N'Khach nang hang Presidential Family')
     ) AS d(ma_dp, ma_cu, ma_moi, ma_nv, doi_luc, chenh_lech, ly_do)
         JOIN dat_phong          dp   ON dp.ma_dat_phong   = d.ma_dp
         JOIN dat_phong_chi_tiet dpct ON dpct.id_dat_phong = dp.id_dat_phong
         JOIN phong              pc   ON pc.ma_phong        = d.ma_cu
         JOIN phong              pm   ON pm.ma_phong        = d.ma_moi
         JOIN nhan_vien          nv   ON nv.ma_nhan_vien    = d.ma_nv;

-- ---------------------------------------------------------------------
-- LOP 6a: su_dung_dich_vu (10)
-- ---------------------------------------------------------------------
INSERT INTO su_dung_dich_vu(id_dat_phong, id_dich_vu,
                            so_luong, thanh_tien, tien_giam_gia, thoi_gian_su_dung)
SELECT dp.id_dat_phong, dv.id_dich_vu,
       CAST(d.sl AS NUMERIC(10,2)),
       CAST(d.tt   AS NUMERIC(14,0)),
       CAST(d.giam AS NUMERIC(14,0)),
       CAST(d.tgsd AS DATETIME2)
FROM (VALUES
          ('DP2026001','DV_AN_SANG',    1,  180000,      0,'2026-10-02 08:00:00'),
          ('DP2026001','DV_GIAT_UI',    2,  120000,      0,'2026-10-02 10:00:00'),
          ('DP2026002','DV_SPA',        1,  650000,      0,'2026-11-11 15:00:00'),
          ('DP2026002','DV_MINIBAR',    1,   50000,      0,'2026-11-11 21:00:00'),
          ('DP2026004','DV_SPA',        2, 1300000, 650000,'2026-09-16 10:00:00'),
          ('DP2026004','DV_DON_SAN_BAY',1,  450000, 225000,'2026-09-15 12:00:00'),
          ('DP2026004','DV_AN_SANG',    5,  900000, 450000,'2026-09-17 08:00:00'),
          ('DP2026006','DV_GIAT_UI',    3,  180000,      0,'2026-10-21 09:00:00'),
          ('DP2026007','DV_DON_SAN_BAY',1,  450000,      0,'2026-11-08 12:00:00'),
          ('DP2026010','DV_AN_SANG',    2,  360000,      0,'2026-11-02 08:00:00')
     ) AS d(ma_dp, ma_dv, sl, tt, giam, tgsd)
         JOIN dat_phong dp ON dp.ma_dat_phong = d.ma_dp
         JOIN dich_vu   dv ON dv.ma_dich_vu   = d.ma_dv;

-- ---------------------------------------------------------------------
-- LOP 6b: thanh_toan (10)
-- id_the_thanh_vien lay qua ma_qr de dam bao khong bi lech ID
-- ---------------------------------------------------------------------
INSERT INTO thanh_toan(id_dat_phong, id_the_thanh_vien,
                       phuong_thuc_thanh_toan, tien_thanh_toan, trang_thai)
SELECT dp.id_dat_phong, ttv.id_the_thanh_vien,
       d.phuong_thuc,
       CAST(d.tien AS NUMERIC(14,0)),
       d.tt
FROM (VALUES
          ('DP2026001', NULL,                 'TIEN_MAT',  500000,'THANH_CONG'),
          ('DP2026001', NULL,                 'VNPAY',    2073600,'THANH_CONG'),
          ('DP2026002', NULL,                 'TIEN_MAT', 1000000,'THANH_CONG'),
          ('DP2026002','QR-KH002-VANG-2023',  'THE',      3919600,'CHO_XU_LY'),
          ('DP2026004','QR-KH004-KIM-2022',   'THE',     38395000,'THANH_CONG'),
          ('DP2026006', NULL,                 'TIEN_MAT',  500000,'THANH_CONG'),
          ('DP2026006', NULL,                 'VNPAY',    1635600,'THANH_CONG'),
          ('DP2026008', NULL,                 'TIEN_MAT',  200000,'DA_HOAN'),
          ('DP2026010', NULL,                 'VNPAY',    4022000,'THANH_CONG'),
          ('DP2026005', NULL,                 'TIEN_MAT',  500000,'THANH_CONG')
     ) AS d(ma_dp, ma_qr, phuong_thuc, tien, tt)
         JOIN dat_phong           dp  ON dp.ma_dat_phong = d.ma_dp
         LEFT JOIN the_thanh_vien ttv ON ttv.ma_qr       = d.ma_qr;

-- ---------------------------------------------------------------------
-- LOP 6c: lich_su_dat_phong (3) — chi ghi khi huy
-- ---------------------------------------------------------------------
INSERT INTO lich_su_dat_phong(id_dat_phong, id_nguoi_huy,
                              id_ma_giam_gia, so_tien_hoan, ghi_chu)
SELECT dp.id_dat_phong, nv.id_nhan_vien, NULL,
       CAST(d.hoan AS NUMERIC(14,0)), d.ghi_chu
FROM (VALUES
          ('DP2026008','NV005',200000,N'Khach tu huy truoc 48h, hoan toan bo tien coc'),
          ('DP2026009', NULL,       0,N'He thong tu huy do qua han xac nhan'),
          ('DP2026003','NV004',500000,N'Khach yeu cau huy va dat lai')
     ) AS d(ma_dp, ma_nv, hoan, ghi_chu)
         JOIN dat_phong   dp ON dp.ma_dat_phong    = d.ma_dp
         LEFT JOIN nhan_vien nv ON nv.ma_nhan_vien = d.ma_nv;

-- ---------------------------------------------------------------------
-- LOP 6d: danh_gia (5) — chi dat phong DA_TRA_PHONG
-- BIT: 0=hien thi, 1=an danh
-- ---------------------------------------------------------------------
INSERT INTO danh_gia(id_dat_phong, id_khach_hang,
                     diem_danh_gia, binh_luan, bi_an, ly_do_an)
SELECT dp.id_dat_phong, kh.id_khach_hang,
       CAST(d.diem AS SMALLINT),
       d.binh_luan,
       CAST(d.bi_an AS BIT),
       d.ly_do_an
FROM (VALUES
          ('DP2026001','an.nguyen@gmail.com', 5,N'Phong sach se, nhan vien than thien. Se quay lai!',0,NULL),
          ('DP2026004','cam.pham@gmail.com',  5,N'Dich vu 5 sao, xung dang voi gia tien cao cap.',  0,NULL),
          ('DP2026006','linh.dang@gmail.com', 4,N'Phong dep, vi tri tot. An sang co the phong phu hon.',0,NULL),
          ('DP2026010','an.nguyen@gmail.com', 4,N'Trai nghiem tot, se gioi thieu ban be.',           0,NULL),
          ('DP2026007','manh.bui@gmail.com',  3,N'Phong binh thuong, dich vu cham.',                 1,N'Khach yeu cau an danh')
     ) AS d(ma_dp, email, diem, binh_luan, bi_an, ly_do_an)
         JOIN dat_phong  dp ON dp.ma_dat_phong = d.ma_dp
         JOIN khach_hang kh ON kh.email        = d.email;

-- ---------------------------------------------------------------------
-- LOP 7: bang_luong (10) — luong thang 10/2026 cho tat ca nhan vien
-- ---------------------------------------------------------------------
INSERT INTO bang_luong(id_nhan_vien, thoi_gian_nhan,
                       luong_co_ban, phu_cap, khau_tru, luong_tong, trang_thai)
SELECT nv.id_nhan_vien,
       CAST('2026-10-25' AS DATE),
       nv.luong_co_ban, nv.phu_cap, nv.khau_tru,
       nv.luong_co_ban + nv.phu_cap - nv.khau_tru,
       'DA_CHOT'
FROM nhan_vien nv
ORDER BY nv.id_nhan_vien;

-- ---------------------------------------------------------------------
-- LOP 8: bang_thu_chi (10)
-- ---------------------------------------------------------------------
INSERT INTO bang_thu_chi(id_danh_muc, id_bang_luong,
                         id_nguoi_tao, thoi_gian, doanh_thu, ghi_chu)
SELECT dc.id_danh_muc,
       bl.id_bang_luong,
       nv_tao.id_nhan_vien,
       CAST(d.tgian AS DATETIME2),
       CAST(d.dt    AS NUMERIC(14,0)),
       d.ghi_chu
FROM (VALUES
          (N'Luong nhan vien','NV001','NV009','2026-10-25 09:00:00',0,       N'Chi luong T10/2026 - NV001'),
          (N'Luong nhan vien','NV002','NV009','2026-10-25 09:05:00',0,       N'Chi luong T10/2026 - NV002'),
          (N'Luong nhan vien','NV003','NV009','2026-10-25 09:10:00',0,       N'Chi luong T10/2026 - NV003'),
          (N'Luong nhan vien','NV004','NV009','2026-10-25 09:15:00',0,       N'Chi luong T10/2026 - NV004'),
          (N'Luong nhan vien','NV005','NV009','2026-10-25 09:20:00',0,       N'Chi luong T10/2026 - NV005'),
          (N'Dien',            NULL,  'NV009','2026-10-31 17:00:00',0,       N'Hoa don dien T10/2026 - 45tr'),
          (N'Nuoc',            NULL,  'NV009','2026-10-31 17:05:00',0,       N'Hoa don nuoc T10/2026 - 8.5tr'),
          (N'Bao tri, sua chua',NULL, 'NV003','2026-10-15 10:00:00',0,       N'Sua chua dieu hoa tang 1-5'),
          (N'Vat tu, do dung phong',NULL,'NV003','2026-10-10 14:00:00',0,    N'Mua sam do dung phong T10/2026'),
          (N'Chi phi khac',    NULL,  'NV009','2026-10-31 18:00:00',10169800,N'Doanh thu thu phong T10/2026')
     ) AS d(ten_dm, ma_nv_luong, ma_nv_tao, tgian, dt, ghi_chu)
         JOIN danh_muc_chi dc ON dc.ten_danh_muc  = d.ten_dm
         LEFT JOIN nhan_vien    nv_l   ON nv_l.ma_nhan_vien  = d.ma_nv_luong
         LEFT JOIN bang_luong   bl     ON bl.id_nhan_vien     = nv_l.id_nhan_vien
    AND bl.thoi_gian_nhan   = '2026-10-25'
         LEFT JOIN nhan_vien    nv_tao ON nv_tao.ma_nhan_vien = d.ma_nv_tao;

-- =====================================================================
-- CAP NHAT TRANG THAI PHONG
-- =====================================================================
UPDATE phong SET trang_thai = 'DANG_O'
WHERE ma_phong IN ('W1302','W1401');

UPDATE phong SET trang_thai = 'CAN_DON'
WHERE ma_phong IN ('C0301','C0302','C3001','C0204','C0103','C1301');

-- =====================================================================
-- KIEM TRA SO LUONG SAU KHI CHAY
-- =====================================================================
SELECT bang, so_dong FROM (
                              SELECT 'vai_tro'               AS bang, COUNT(*) AS so_dong FROM vai_tro
                              UNION ALL SELECT 'hang_thanh_vien',     COUNT(*) FROM hang_thanh_vien
                              UNION ALL SELECT 'hang_phong',          COUNT(*) FROM hang_phong
                              UNION ALL SELECT 'loai_giuong',         COUNT(*) FROM loai_giuong
                              UNION ALL SELECT 'loai_phong',          COUNT(*) FROM loai_phong
                              UNION ALL SELECT 'toa_nha',             COUNT(*) FROM toa_nha
                              UNION ALL SELECT 'tang',                COUNT(*) FROM tang
                              UNION ALL SELECT 'phong',               COUNT(*) FROM phong
                              UNION ALL SELECT 'tien_nghi',           COUNT(*) FROM tien_nghi
                              UNION ALL SELECT 'dich_vu',             COUNT(*) FROM dich_vu
                              UNION ALL SELECT 'danh_muc_chi',        COUNT(*) FROM danh_muc_chi
                              UNION ALL SELECT 'nhan_vien',           COUNT(*) FROM nhan_vien
                              UNION ALL SELECT 'khach_hang',          COUNT(*) FROM khach_hang
                              UNION ALL SELECT 'ma_giam_gia',         COUNT(*) FROM ma_giam_gia
                              UNION ALL SELECT 'the_thanh_vien',      COUNT(*) FROM the_thanh_vien
                              UNION ALL SELECT 'tien_nghi_phong',     COUNT(*) FROM tien_nghi_phong
                              UNION ALL SELECT 'dat_phong',           COUNT(*) FROM dat_phong
                              UNION ALL SELECT 'dat_phong_chi_tiet',  COUNT(*) FROM dat_phong_chi_tiet
                              UNION ALL SELECT 'khach_luu_tru',       COUNT(*) FROM khach_luu_tru_chi_tiet
                              UNION ALL SELECT 'nhiem_vu_don_phong',  COUNT(*) FROM nhiem_vu_don_phong
                              UNION ALL SELECT 'lich_su_doi_phong',   COUNT(*) FROM lich_su_doi_phong
                              UNION ALL SELECT 'su_dung_dich_vu',     COUNT(*) FROM su_dung_dich_vu
                              UNION ALL SELECT 'lich_su_dat_phong',   COUNT(*) FROM lich_su_dat_phong
                              UNION ALL SELECT 'thanh_toan',          COUNT(*) FROM thanh_toan
                              UNION ALL SELECT 'danh_gia',            COUNT(*) FROM danh_gia
                              UNION ALL SELECT 'bang_luong',          COUNT(*) FROM bang_luong
                              UNION ALL SELECT 'bang_thu_chi',        COUNT(*) FROM bang_thu_chi
                          ) t ORDER BY bang;

COMMIT TRANSACTION;
