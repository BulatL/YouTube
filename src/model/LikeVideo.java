package model;

import java.util.Date;

public class LikeVideo {


	public int id;
	public Boolean like;
	public String datumKreiranja;
	public Korisnik vlasnikLike;
	public Video video;
	public Boolean obrisan;
	
	public LikeVideo (){
		id = 0;
		like = false;
		datumKreiranja = "";
		vlasnikLike = new Korisnik();
		video = new Video();
		obrisan = false;
	}

	public LikeVideo(int id, boolean like, String datumKreiranja, Korisnik vlasnikLike, Video video, Boolean obrisan) {
		super();
		this.id = id;
		this.like = like;
		this.datumKreiranja = datumKreiranja;
		this.vlasnikLike = vlasnikLike;
		this.video = video;
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

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Boolean getObrisan() {
		return obrisan;
	}

	public void setObrisan(Boolean obrisan) {
		this.obrisan = obrisan;
	}
	
}