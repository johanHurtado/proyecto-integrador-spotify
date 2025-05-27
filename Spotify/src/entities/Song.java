public class Song {
    private Integer idSong;
    private String title;
    private double timeLength;
    public Song() {
    
    }
    public Song(Integer idSong, String title, double timeLength) {
        this.idSong = idSong;
        this.title = title;
        this.timeLength = timeLength;
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
    public double getTimeLength() {
        return timeLength;
    }
    public void setTimeLength(double timeLength) {
        this.timeLength = timeLength;
    }
    @Override
    public String toString() {
        return "Song [idSong=" + idSong + ", title=" + title + ", timeLength=" + timeLength + "]";
    }

    
}
