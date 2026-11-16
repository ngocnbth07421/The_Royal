package repository;

import entity.danh_gia;
import entity.dat_phong;
import entity.khach_hang;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;


public class danh_giaRepository {

    private Session session;

    public danh_giaRepository(){
        session = HibernateConfig.getFACTORY().openSession();
    }

    public List<danh_gia> getAll(){
        return session.createQuery("FROM danh_gia").list();
    }

    public danh_gia getOne(Integer id){
        return session.find(danh_gia.class, id);
    }

    public void add(danh_gia dg){
        try {
            session.getTransaction().begin();
            session.save(dg);
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void update(danh_gia dg){
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
    public List<dat_phong> getAllDatPhong() {
        return session.createQuery("FROM dat_phong", dat_phong.class).list();
    }

    public List<khach_hang> getAllKhachHang() {
        return session.createQuery("FROM khach_hang", khach_hang.class).list();
    }

    public static void main(String[] args) {
        System.out.println(new danh_giaRepository().getAll());
    }
}
