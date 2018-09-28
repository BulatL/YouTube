package model;

public class LikeKomentar {

	public int id;
	public Boolean like;
	public String datumKreiranja;
	public Korisnik vlasnikLike;
	public Komentar komentar;
	public Boolean obrisan;
	
	public LikeKomentar (){
		id = 0;
		like = false;
		datumKreiranja = "";
		vlasnikLike = new Korisnik();
		komentar = new Komentar();
		obrisan = false;
	}

	public LikeKomentar(int id, boolean like, String datumKreiranja, Korisnik vlasnikLike, Komentar komentar,
			Boolean obrisan) {
		super();
		this.id = id;
		this.like = like;
		this.datumKreiranja = datumKreiranja;
		this.vlasnikLike = vlasnikLike;
		this.komentar = komentar;
		this.obrisan = obrisan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getLike() {
		return like;
	}

	public void setLike(Boolean like) {
		this.like = like;
	}

	public String getDatumKreiranja() {
		return datumKreiranja;
	}

	public void setDatumKreiranja(String datumKreiranja) {
		this.datumKreiranja = datumKreiranja;
	}

	public Korisnik getVlasnikLike() {
		return vlasnikLike;
	}

	public void setVlasnikLike(Korisnik vlasnikLike) {
		this.vlasnikLike = vlasnikLike;
	}

	public Komentar getKomentar() {
		return komentar;
	}

	public void setKomentar(Komentar komentar) {
		this.komentar = komentar;
	}

	public Boolean getObrisan() {
		return obrisan;
	}

	public void setObrisan(Boolean obrisan) {
		this.obrisan = obrisan;
	}
}