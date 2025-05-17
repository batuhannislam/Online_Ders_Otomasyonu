/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_ders_otomasyonu;

/**
 *
 * @author batuh
 */
public class Session {
    private static int kullaniciID;
    private static int rolID;

    public static void setKullaniciID(int id) {
        kullaniciID = id;
    }

    public static int getKullaniciID() {
        return kullaniciID;
    }

    public static void setRolID(int id) {
        rolID = id;
    }

    public static int getRolID() {
        return rolID;
    }
}
