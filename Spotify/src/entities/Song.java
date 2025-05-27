public class Song {
    private Integer idSong;
    private String title;
    private String description;
    private double timeLength;
    private Artist artist;
    private Gender gender;
    public Song() {
    
    }
    public Song(Integer idSong, String title, String description, double timeLength, Artist artist, Gender gender) {
        this.idSong = idSong;
        this.title = title;
        this.description = description;
        this.timeLength = timeLength;
        this.artist = artist;
        this.gender = gender;
    }
    public Integer getIdSong() {
        return idSong;
    }
    public void setIdSong(Integer idSong) {
        this.idSong = idSong;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getTimeLength() {
        return timeLength;
    }
    public void setTimeLength(double timeLength) {
        this.timeLength = timeLength;
    }
    public Artist getArtist() {
        return artist;
    }
    public void setArtist(Artist artist) {
        this.artist = artist;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    @Override
    public String toString() {
        return "Song [idSong=" + idSong + ", title=" + title + ", description=" + description + ", timeLength="
                + timeLength + ", artist=" + artist + ", gender=" + gender + "]";
    }
    
}
