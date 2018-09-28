package model;

import java.io.Serializable;

public class Video implements Serializable {
	
	public enum Vidljivost{PUBLIC, UNLISTED, PRIVATE};
	
	public int id;
	public String videoUrl;
	public String slika;
	public String naziv;
	public String opis;
	public Vidljivost vidljivost;
	public Boolean blokiran;
	public Boolean rejting;
	public Boolean komentar;
	public int brojPregleda;
	public int brojLajkova;
	public String datumKreiranja;
	public Korisnik vlasnik;
	public Boolean obrisan;
	
	public Video(){
		id = 0;
		videoUrl = "";
		slika = "";
		naziv = "";
		opis = ""; 
		vidljivost = Vidljivost.PUBLIC;
		blokiran = false;
		rejting = true;
		komentar = true;
		brojPregleda = 0;
		brojLajkova = 0;
		datumKreiranja = "";
		vlasnik = new Korisnik();
		obrisan = false;
	}

	public Video(int id, String videoUrl, String slika, String naziv, String opis, Vidljivost vidljivost,
			Boolean blokiran, Boolean rejting, Boolean komentar, int brojPregleda, int brojLajkova, String datumKreiranja,
			Korisnik vlasnik, Boolean obrisan) {
		super();
		this.id = id;
		this.videoUrl = videoUrl;
		this.slika = slika;
		this.naziv = naziv;
		this.opis = opis;
		this.vidljivost = vidljivost;
		this.blokiran = blokiran;
		this.rejting = rejting;
		this.komentar = komentar;
		this.brojPregleda = brojPregleda;
		this.brojLajkova = brojLajkova;
		this.datumKreiranja = datumKreiranja;
		this.vlasnik = vlasnik;
		this.obrisan = obrisan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public Vidljivost getVidljivost() {
		return vidljivost;
	}

	public void setVidljivost(Vidljivost vidljivost) {
		this.vidljivost = vidljivost;
	}

	public Boolean getBlokiran() {
		return blokiran;
	}

	public void setBlokiran(Boolean blokiran) {
		this.blokiran = blokiran;
	}

	public Boolean getRejting() {
		return rejting;
	}

	public void setRejting(Boolean rejting) {
		this.rejting = rejting;
	}

	public Boolean getKomentar() {
		return komentar;
	}

	public void setKomentar(Boolean komentar) {
		this.komentar = komentar;
	}

	public int getBrojPregleda() {
		return brojPregleda;
	}

	public void setBrojPregleda(int brojPregleda) {
		this.brojPregleda = brojPregleda;
	}

	public int getBrojLajkova() {
		return brojLajkova;
	}

	public void setBrojLajkova(int brojLajkova) {
		this.brojLajkova = brojLajkova;
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

	public Boolean getObrisan() {
		return obrisan;
	}

	public void setObrisan(Boolean obrisan) {
		this.obrisan = obrisan;
	}
	
	@Override
	public String toString() {
		return "Video [id=" + id + ", videoUrl=" + videoUrl + ", slika=" + slika + ", naziv=" + naziv + ", opis=" + opis
				+ ", vidljivost=" + vidljivost + ", blokiran=" + blokiran + ", rejting=" + rejting + ", komentar="
				+ komentar + ", brojPregleda=" + brojPregleda + ", brojLajkova=" + brojLajkova + ", datumKreiranja="
				+ datumKreiranja + ", vlasnik=" + vlasnik + ", obrisan=" + obrisan + "]";
	}

	
}