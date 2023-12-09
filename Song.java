public class Song {
    private int id;
    private String title;
    private String artist;
    private String filePath;
    private String thumbnail;

    public Song() {
    }

    public Song(String title, String artist, String filePath, String thumbnail) {
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.thumbnail = thumbnail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void Thumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String Thumbnail() {
        return thumbnail;
    }

    public String toString() {
        return title + " - " + artist;
    }
}
