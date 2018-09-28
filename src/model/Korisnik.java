package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Korisnik implements Serializable {

	public enum VrstaKorisnika {USER, ADMIN};
	
	public int id;
	public String korisnickoIme;
	public String lozinka;
	public String ime;
	public String prezime;
	public String email;
	public String opisKanala;
	public String datum;
	public Boolean blokiran;
	public Collection<Korisnik> kogaPratiKorisnik;
	public Collection<Korisnik> koNjegaPrati;
	public Collection<Video> listaVidea;
	public Collection<Video> lajkovaniVideo;
	public Collection<Komentar> listaKomentara;
	public VrstaKorisnika vrstaKorisnika;
	public Boolean obrisan;
	
	public Korisnik (){
		id = 0;
		korisnickoIme = "";
		lozinka = "";
		ime = "";
		prezime = "";
		email = "";
		opisKanala = "";
		datum = "";
		blokiran = false;
		kogaPratiKorisnik = new ArrayList<Korisnik>();
		koNjegaPrati = new ArrayList<Korisnik>();
		listaVidea = new ArrayList<Video>();
		lajkovaniVideo = new ArrayList<Video>();
		listaKomentara = new ArrayList<Komentar>();
		vrstaKorisnika = VrstaKorisnika.USER;
		obrisan = false;
	}

	public Korisnik(int id, String korisnickoIme, String lozinka, String ime, String prezime, String email,
			String opisKanala, String datum, Boolean blokiran, Collection<Korisnik> kogaPratiKorisnik,
			Collection<Korisnik> koNjegaPrati, Collection<Video> listaVidea, Collection<Video> lajkovaniVideo,
			Collection<Komentar> listaKomentara, VrstaKorisnika vrstaKorisnika,
			Boolean obrisan) {
		super();
		this.id = id;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.opisKanala = opisKanala;
		this.datum = datum;
		this.blokiran = blokiran;
		this.kogaPratiKorisnik = kogaPratiKorisnik;
		this.koNjegaPrati = koNjegaPrati;
		this.listaVidea = listaVidea;
		this.lajkovaniVideo = lajkovaniVideo;
		this.listaKomentara = listaKomentara;
		this.vrstaKorisnika = vrstaKorisnika;
		this.obrisan = obrisan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpisKanala() {
		return opisKanala;
	}

	public void setOpisKanala(String opisKanala) {
		this.opisKanala = opisKanala;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public Boolean getBlokiran() {
		return blokiran;
	}

	public void setBlokiran(Boolean blokiran) {
		this.blokiran = blokiran;
	}

	public Collection<Korisnik> getKogaPratiKorisnik() {
		return kogaPratiKorisnik;
	}

	public void setKogaPratiKorisnik(Collection<Korisnik> kogaPratiKorisnik) {
		this.kogaPratiKorisnik = kogaPratiKorisnik;
	}

	public Collection<Korisnik> getKoNjegaPrati() {
		return koNjegaPrati;
	}

	public void setKoNjegaPrati(Collection<Korisnik> koNjegaPrati) {
		this.koNjegaPrati = koNjegaPrati;
	}

	public Collection<Video> getListaVidea() {
		return listaVidea;
	}

	public void setListaVidea(Collection<Video> listaVidea) {
		this.listaVidea = listaVidea;
	}

	public Collection<Video> getLajkovaniVideo() {
		return lajkovaniVideo;
	}

	public void setLajkovaniVideo(Collection<Video> lajkovaniVideo) {
		this.lajkovaniVideo = lajkovaniVideo;
	}

	public Collection<Komentar> getListaKomentara() {
		return listaKomentara;
	}

	public void setListaKomentara(Collection<Komentar> listaKomentara) {
		this.listaKomentara = listaKomentara;
	}

	public VrstaKorisnika getVrstaKorisnika() {
		return vrstaKorisnika;
	}

	public void setVrstaKorisnika(VrstaKorisnika vrstaKorisnika) {
		this.vrstaKorisnika = vrstaKorisnika;
	}

	public Boolean getObrisan() {
		return obrisan;
	}

	public void setObrisan(Boolean obrisan) {
		this.obrisan = obrisan;
	}
}