package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];

    public Playlist(String name) {
        this.name = name;
    }
    public String getName() { return name; }

    void addSong(Song song) {
        int n = songs.length;
        Song[] newsongs = new Song[n + 1];
        System.arraycopy(songs, 0, newsongs, 0, n);
        newsongs[n] = song;
        songs = newsongs;
    }

    void printSortedByTitle() {
        Song[] clone = songs.clone();
        Arrays.sort(clone);
        for(Song s: clone) {
            System.out.println(s);
        }
    }
    void printSortedByDuration() {
        Song[] clone = songs.clone();
        Arrays.sort(clone, new SongDurationComparator());
        for(Song s: clone) {
            System.out.println(s);
        }
    }

    int getTotalDuration() {
        int sum = 0;
        for(Song s: songs) {
            sum += s.durationSeconds();
        }
        return sum;
    }
}
