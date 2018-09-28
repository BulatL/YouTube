package model;

public class Komentar {
	
	public int id;
	public String sadrzaj;
	public String datumKreiranja;
	public Korisnik vlasnik;
	public Video video;
	public int brojLajkova;
	public Boolean obrisan;
	
	public Komentar(){
		id = 0;
		sadrzaj = "";
		datumKreiranja = "";
		vlasnik = new Korisnik();
		video = new Video();
		brojLajkova = 0;
		obrisan = false;
	}

	public Komentar(int id, String sadrzaj, String datumKreiranja, Korisnik vlasnik, Video video, int brojLajkova, Boolean obrisan) {
		super();
		this.id = id;
		this.sadrzaj = sadrzaj;
		this.datumKreiranja = datumKreiranja;
		this.vlasnik = vlasnik;
		this.video = video;
		this.brojLajkova = brojLajkova;
		this.obrisan = obrisan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSadrzaj() {
		return sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getDatumKreiranja() {
		return datumKreiranja;
	}

	public void setDatumKreiranja(String datumKreiranja) {
		this.datumKreiranja = datumKreiranja;
	}

	public Korisnik getVlasnik() {
		return vlasnik;
	}

	public void setVlasnik(Korisnik vlasnik) {
		this.vlasnik = vlasnik;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public int getBrojLajkova() {
		return brojLajkova;
	}

	public void setBrojLajkova(int brojLajkova) {
		this.brojLajkova = brojLajkova;
	}

	public Boolean getObrisan() {
		return obrisan;
	}

	public void setObrisan(Boolean obrisan) {
		this.obrisan = obrisan;
	}

	@Override
	public String toString() {
		return "Komentar [id=" + id + ", sadrzaj=" + sadrzaj + ", datumKreiranja=" + datumKreiranja + ", vlasnik="
				+ vlasnik + ", video=" + video + ", brojLajkova=" + brojLajkova + ", obrisan=" + obrisan + "]";
	}
	
	
}