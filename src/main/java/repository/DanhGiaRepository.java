package repository;

import entity.DanhGia;
import entity.DatPhong;
import entity.KhachHang;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;


public class DanhGiaRepository {

    private Session session;

    public DanhGiaRepository(){
        session = HibernateConfig.getFACTORY().openSession();
    }

    public List<DanhGia> getAll(){
        return session.createQuery("FROM DanhGia", DanhGia.class).list();
    }

    public DanhGia getOne(Integer id){
        return session.find(DanhGia.class, id);
    }

    public void add(DanhGia dg){
        try {
            session.getTransaction().begin();
            session.save(dg);
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void update(DanhGia dg){
        try {
            session.getTransaction().begin();
            session.merge(dg);
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }
    public void delete(Integer id){
        try {
            session.getTransaction().begin();
            session.delete(this.getOne(id));
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }
    public List<DatPhong> getAllDatPhong() {
        return session.createQuery("FROM DatPhong", DatPhong.class).list();
    }

    public List<KhachHang> getAllKhachHang() {
        return session.createQuery("FROM KhachHang", KhachHang.class).list();
    }

    public static void main(String[] args) {
        System.out.println(new DanhGiaRepository().getAll());
    }
}

