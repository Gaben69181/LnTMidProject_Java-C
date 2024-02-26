import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;

public class Aplikasi {
    static ArrayList<Karyawan> daftarKaryawan = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int[] jabatanCounter = new int[3];
    static Random random = new Random();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Aplikasi Manajemen Karyawan PT. ChipiChapa");
            System.out.println("1. Insert Data Karyawan");
            System.out.println("2. View Data Karyawan");
            System.out.println("3. Update Data Karyawan");
            System.out.println("4. Delete Data Karyawan");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu [0-4]: ");
            String input = scanner.next();
            if (input.matches("[0-4]")) {
                int pilihan = Integer.parseInt(input);
                switch (pilihan) {
                    case 1:
                        insertData();
                        break;
                    case 2:
                        viewData();
                        break;
                    case 3:
                        updateData();
                        break;
                    case 4:
                        deleteData();
                        break;
                    case 0:
                        System.out.println("Aplikasi ditutup.");
                        System.exit(0);
                }
            } else {
                System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                scanner.nextLine();
            }
        }
    }

    static void jabatanCounter(String jabatan) {
        int index = getJabatanIndex(jabatan);
        jabatanCounter[index]++;
    }
    
    static int getJabatanIndex(String jabatan) {
        switch (jabatan.toLowerCase()) {
            case "manager":
                return 0;
            case "supervisor":
                return 1;
            case "admin":
                return 2;
            default:
                return -1;
        }
    }
    
    static void insertData() {
        System.out.println("Masukkan Data Karyawan");
        String kode = generateKode();
        String nama = null;
        boolean isValidNama = false;

        while (!isValidNama) {
            System.out.print("Masukkan nama karyawan (minimal 3 huruf): ");
            nama = scanner.next();

            if (nama.length() >= 3 && nama.matches("^[a-zA-Z]+$")) {
                isValidNama = true;
            } else {
                System.out.println("Nama karyawan harus terdiri dari minimal 3 huruf dan tanpa angka.");
            }
        }

        // Input Jenis Kelamin
        System.out.print("Masukkan jenis kelamin (Laki-laki | Perempuan): ");
        String jenisKelamin = scanner.next();
        while (!jenisKelamin.equals("Laki-laki") && !jenisKelamin.equals("Perempuan")) {
            System.out.println("Jenis kelamin yang dimasukkan tidak valid.");
            System.out.print("Masukkan jenis kelamin (Laki-Laki | Perempuan): ");
            jenisKelamin = scanner.next();
        }

        // Input Jabatan
        System.out.print("Masukkan jabatan (Manager | Supervisor | Admin): ");
        String jabatan = scanner.next();
        while (!jabatan.equals("Manager") && !jabatan.equals("Supervisor") && !jabatan.equals("Admin")) {
            System.out.println("Jabatan yang dimasukkan tidak valid.");
            System.out.print("Masukkan jabatan (Manager | Supervisor | Admin): ");
            jabatan = scanner.next();
        }

        int gaji = getGajiByJabatan(jabatan);
        jabatanCounter(jabatan);
        bonusGaji(jabatan, jabatanCounter[getJabatanIndex(jabatan)]);

        Karyawan karyawan = new Karyawan(kode, nama, jenisKelamin, jabatan, gaji);
        daftarKaryawan.add(karyawan);

        System.out.println("Berhasil menambahkan karyawan dengan id " + karyawan.kode);
        
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    static String generateKode() {
        // Generate 2 huruf alfabet random
        char[] hurufRandom = new char[2];
        for (int i = 0; i < 2; i++) {
            hurufRandom[i] = (char) ('A' + random.nextInt(26));
        }

        // Generate 4 digit angka random
        int angkaRandom = 1000 + random.nextInt(9000);

        return new String(hurufRandom) + "-" + angkaRandom;
    }

    static int getGajiByJabatan(String jabatan) {
        switch (jabatan.toLowerCase()) {
            case "manager":
                return 8000000;
            case "supervisor":
                return 6000000;
            case "admin":
                return 4000000;
            default:
                return 0;
        }
    }
    
    static long countKaryawanByJabatan(Predicate<Karyawan> predicate) {
        return daftarKaryawan.stream().filter(predicate).count();
    }

    static void bonusGaji(String jabatan, int jumlahKaryawanSama) {
        if (jumlahKaryawanSama > 0) {
            double bonus = getBonusByJabatan(jabatan);
            int karyawanBonusCount = (jumlahKaryawanSama >= 4) ? (jumlahKaryawanSama - (jumlahKaryawanSama % 3)) : 0;
            if (karyawanBonusCount > 0) {
                System.out.println("Bonus sebesar " + bonus + "% telah diberikan kepada " + karyawanBonusCount + " karyawan dengan id " + getBonusKoma(jabatan));
                jabatanCounter[getJabatanIndex(jabatan)] -= karyawanBonusCount;
                for (int i = 0; i < karyawanBonusCount; i++) {
                    Karyawan karyawan = daftarKaryawan.get(i);
                    if (karyawan.getJabatan().equals(jabatan)) {
                        double gajiSebelumnya = karyawan.getGaji();
                        double bonusGaji = gajiSebelumnya * bonus / 100;
                        karyawan.setGaji(gajiSebelumnya + bonusGaji);
                    }
                }
            }
        }
    }

    static String getBonusKoma(String jabatan) {
        StringBuilder bonusRecipients = new StringBuilder();
        int karyawanBonusCount = Math.min((int) countKaryawanByJabatan(karyawan -> karyawan.getJabatan().equals(jabatan)), 3);
        for (int i = 0; i < karyawanBonusCount; i++) {
            Karyawan karyawan = daftarKaryawan.get(i);
            if (karyawan.getJabatan().equals(jabatan)) {
                if (i > 0) {
                    bonusRecipients.append(", ");
                }
                bonusRecipients.append(karyawan.getKode());
            }
        }
        return bonusRecipients.toString();
    }
    
    static double getBonusByJabatan(String jabatan) {
        switch (jabatan.toLowerCase()) {
            case "manager":
                return 10.0;
            case "supervisor":
                return 7.5;
            case "admin":
                return 5.0;
            default:
                return 0.0;
        }
    }

    static void viewData() {
    	System.out.println("=== View Data Karyawan ===");
    	
        // Sorting data karyawan berdasarkan nama (ascending)
        daftarKaryawan.sort(Comparator.comparing(Karyawan::getNama));
        System.out.println("|==========================================================================================|");
        System.out.format("|%-5s|%-15s|%-20s|%-15s|%-15s|%-15s|%n", "No.", "Kode Karyawan", "Nama Karyawan", "Jenis Kelamin", "Jabatan", "Gaji Karyawan");
        System.out.println("|==========================================================================================|");
 
        // Menampilkan data karyawan
        for (int i = 0; i < daftarKaryawan.size(); i++) {
        	Karyawan karyawan = daftarKaryawan.get(i);
            System.out.format("|%-5s|%-15s|%-20s|%-15s|%-15s|%-15s|%n",
                    i + 1, karyawan.kode, karyawan.nama, karyawan.jenisKelamin, karyawan.jabatan, karyawan.gaji);
        }
        System.out.println("|==========================================================================================|");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();  
        scanner.nextLine();  
    }

    static void updateData() {
    	System.out.println("=== Update Data Karyawan ===");
        daftarKaryawan.sort(Comparator.comparing(Karyawan::getNama));

     // Menampilkan tabel header
        System.out.println("|==========================================================================================|");
        System.out.format("|%-5s|%-15s|%-20s|%-15s|%-15s|%-15s|%n", "No.", "Kode Karyawan", "Nama Karyawan", "Jenis Kelamin", "Jabatan", "Gaji Karyawan");
        System.out.println("|==========================================================================================|");
       
        // Menampilkan data karyawan
        for (int i = 0; i < daftarKaryawan.size(); i++) {
        	Karyawan karyawan = daftarKaryawan.get(i);
            System.out.format("|%-5s|%-15s|%-20s|%-15s|%-15s|%-15s|%n",
                    i + 1, karyawan.kode, karyawan.nama, karyawan.jenisKelamin, karyawan.jabatan, karyawan.gaji);
        }
        System.out.println("|==========================================================================================|");

        // Meminta input nomor untuk memilih data yang akan diupdate
        System.out.print("Masukkan nomor urutan karyawan yang ingin diupdate (0 untuk batal): ");
        int selectedNumber;

        while (true) {
            if (scanner.hasNextInt()) {
                selectedNumber = scanner.nextInt();
                if (selectedNumber >= 0 && selectedNumber <= daftarKaryawan.size()) {
                    break;
                } else {
                    System.out.println("Nomor yang dimasukkan tidak valid.");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    updateData();
                }
            } else {
                System.out.println("Masukkan angka yang valid.");
                System.out.println("Press Enter to continue...");
                scanner.next();  // clear buffer
                scanner.nextLine();
                scanner.nextLine();
                updateData();
            }
        }

        if (selectedNumber == 0) {
            System.out.println("Update dibatalkan.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            scanner.nextLine();
            main(null);
        }
      
        Karyawan selectedKaryawan = daftarKaryawan.get(selectedNumber - 1);

         // Input Nama Karyawan
            String newNama = null;
            boolean isValidNama = false;

            while (!isValidNama) {
                System.out.print("Masukkan nama karyawan (minimal 3 huruf): ");
                newNama = scanner.next();

                if (newNama.length() >= 3 && newNama.matches("^[a-zA-Z]+$")) {
                    isValidNama = true;
                } else {
                    System.out.println("Nama karyawan harus terdiri dari minimal 3 huruf dan tanpa angka.");
                }
            }

            // Input Jenis Kelamin
            System.out.print("Masukkan jenis kelamin (Laki-laki | Perempuan): ");
            String newJenisKelamin = scanner.next();
            while (!newJenisKelamin.equals("Laki-laki") && !newJenisKelamin.equals("Perempuan")) {
                System.out.println("Jenis kelamin yang dimasukkan tidak valid.");
                System.out.print("Masukkan jenis kelamin (Laki-Laki | Perempuan): ");
                newJenisKelamin = scanner.next();
            }

            // Input Jabatan
            System.out.print("Masukkan jabatan (Manager | Supervisor | Admin): ");
            String newJabatan = scanner.next();
            while (!newJabatan.equals("Manager") && !newJabatan.equals("Supervisor") && !newJabatan.equals("Admin")) {
                System.out.println("Jabatan yang dimasukkan tidak valid.");
                System.out.print("Masukkan jabatan (Manager | Supervisor | Admin): ");
                newJabatan = scanner.next();
            }
            
            jabatanCounter(newJabatan);
            bonusGaji(newJabatan, jabatanCounter[getJabatanIndex(newJabatan)]);
         
            selectedKaryawan.nama = newNama;
            selectedKaryawan.jenisKelamin = newJenisKelamin;
            selectedKaryawan.jabatan = newJabatan;

        System.out.println("Berhasil mengupdate karyawan dengan id " + selectedKaryawan.kode);      
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    static void deleteData() {
    	System.out.println("=== Delete Data Karyawan ===");
    	daftarKaryawan.sort(Comparator.comparing(Karyawan::getNama));
        System.out.println("|==========================================================================================|");
        System.out.format("|%-5s|%-15s|%-20s|%-15s|%-15s|%-15s|%n", "No.", "Kode Karyawan", "Nama Karyawan", "Jenis Kelamin", "Jabatan", "Gaji Karyawan");
        System.out.println("|==========================================================================================|");
        for (int i = 0; i < daftarKaryawan.size(); i++) {
        	Karyawan karyawan = daftarKaryawan.get(i);
            System.out.format("|%-5s|%-15s|%-20s|%-15s|%-15s|%-15s|%n",
                    i + 1, karyawan.kode, karyawan.nama, karyawan.jenisKelamin, karyawan.jabatan, karyawan.gaji);
        }
        System.out.println("|==========================================================================================|");
        System.out.print("Masukkan nomor urutan karyawan yang ingin dihapus (0 untuk batal): ");
        int selectedNumber;
        while (true) {
            if (scanner.hasNextInt()) {
                selectedNumber = scanner.nextInt();
                if (selectedNumber >= 0 && selectedNumber <= daftarKaryawan.size()) {
                    break;
                } else {
                    System.out.println("Nomor yang dimasukkan tidak valid.");
                    scanner.nextLine();
                    scanner.nextLine();
                    deleteData();
                }
            } else {
            	System.out.println("Masukkan angka yang valid.");
                System.out.println("Press Enter to continue...");
                scanner.next();  // clear buffer
                scanner.nextLine();
                scanner.nextLine();
                deleteData();
            }
        }
        if (selectedNumber == 0) {
            System.out.println("Penghapusan data dibatalkan.");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            scanner.nextLine();
            main(null);
        }

        // Dapatkan data karyawan yang akan dihapus
        Karyawan karyawanToDelete = daftarKaryawan.get(selectedNumber - 1);
        jabatanCounter[getJabatanIndex(karyawanToDelete.getJabatan())]--; // Kurangi counter gaji tambahan
        bonusGaji(karyawanToDelete.getJabatan(), jabatanCounter[getJabatanIndex(karyawanToDelete.getJabatan())]);
        daftarKaryawan.remove(karyawanToDelete);   
        System.out.println("Karyawan dengan kode " + karyawanToDelete.kode + " berhasil dihapus");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }
}
