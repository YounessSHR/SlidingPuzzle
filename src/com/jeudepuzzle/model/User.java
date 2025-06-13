package com.jeudepuzzle.model;

public class User {
    private int id;
    private String username;
    private String email;
    private int score;

    public User(int id, String username, String email, int score) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.score = score;
    }


    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public int getScore() { return score; }


    public void setId(int id) {
		this.id = id;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setScore(int score) {
		this.score = score;
	}


	@Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username=\'" + username + '\'' +
               ", email=\'" + email + '\'' +
               ", score=" + score +
               '}';
    }
}


/**
 *  ************************************************ PuzzleWorld ************************************************
 *
 *  Développé par:
 *   - Abdelali ITTAS
 *   - Abdelouahab Mjahdi
 *   - Youness SAHRAOUI
 *   - Mohammed MAATAOUI BELABBES
 *
 *  Version: bêta
 *  (c) 2025
 *
 *  ************************************************************************************************************
 */