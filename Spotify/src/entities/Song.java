package entities;

/**
 * Entity class that mirrors the “canciones” table.
 * MP3 is stored as a MEDIUMBLOB (byte []) in the column “archivo_mp3”.
 */
public class Song {

    private int     id;
    private String  title;
    private String  description;
    private double  duration;      // seconds
    private int     artistId;
    private int     genreId;
    private byte[]  coverArt;      // optional
    private byte[]  mp3Bytes;      // the actual MP3 file

    public Song() { }

    public Song(int id, String title, String description, double duration,
                int artistId, int genreId, byte[] coverArt, byte[] mp3Bytes) {
        this.id         = id;
        this.title      = title;
        this.description= description;
        this.duration   = duration;
        this.artistId   = artistId;
        this.genreId    = genreId;
        this.coverArt   = coverArt;
        this.mp3Bytes   = mp3Bytes;
    }

    /* ======= Getters & setters ======= */

    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }

    public String getTitle()                 { return title; }
    public void setTitle(String title)       { this.title = title; }

    public String getDescription()           { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDuration()              { return duration; }
    public void setDuration(double duration) { this.duration = duration; }

    public int getArtistId()                 { return artistId; }
    public void setArtistId(int artistId)    { this.artistId = artistId; }

    public int getGenreId()                  { return genreId; }
    public void setGenreId(int genreId)      { this.genreId = genreId; }

    public byte[] getCoverArt()              { return coverArt; }
    public void setCoverArt(byte[] coverArt) { this.coverArt = coverArt; }

    public byte[] getMp3Bytes()              { return mp3Bytes; }
    public void setMp3Bytes(byte[] mp3Bytes) { this.mp3Bytes = mp3Bytes; }
}
