public class Karyawan {
    String kode;
    String nama;
    String jenisKelamin;
    String jabatan;
    int gaji;

    public Karyawan(String kode, String nama, String jenisKelamin, String jabatan, int gaji) {
        this.kode = kode;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.jabatan = jabatan;
        this.gaji = gaji;
    }

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getJenisKelamin() {
		return jenisKelamin;
	}

	public void setJenisKelamin(String jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}

	public String getJabatan() {
		return jabatan;
	}

	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	public int getGaji() {
		return gaji;
	}

	public void setGaji(double gaji) {
		this.gaji = (int) gaji;
	}
    
    
}