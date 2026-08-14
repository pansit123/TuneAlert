import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Song — ADT แทน "เพลง" หนึ่งเพลง (Immutable Class)
 */
public final class Song {

    private final String title;
    private final String artist;
    private final List<String> tags;

    /**
     * สร้างเพลง
     * @param title ชื่อของเพลง
     * @param artist นักร้อง
     * @param tags ชื่อแท็กของรายการเพลง
     * @throws IllegalArgumentException เมื่อ title/artist/tags หรือสมาชิกใน tags เป็น null หรือว่าง
     */
    public Song(String title, String artist, List<String> tags) {
        // 1. Validate primitive fields
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title error");
        }
        if (artist == null || artist.isEmpty()) {
            throw new IllegalArgumentException("Artist error");
        }
        if (tags == null) {
            throw new IllegalArgumentException("Tags list cannot be null");
        }

        // 2. Defensive Copy ขาเข้าก่อน (ป้องกัน Thread Safety / Mutation)
        List<String> tagsCopy = new ArrayList<>(tags);

        // 3. Validate สมาชิกใน List ที่คัดลอกมาแล้ว (ปลอดภัยจาก NullPointerException)
        for (String tag : tagsCopy) {
            if (tag == null || tag.isEmpty()) {
                throw new IllegalArgumentException("Tag element cannot be null or empty");
            }
        }

        this.title = title;
        this.artist = artist;
        this.tags = List.copyOf(tagsCopy); // ใช้ List.copyOf เพื่อให้ได้ Unmodifiable List ภายใน

        checkRep(); // ตรวจสอบความถูกต้องของ Rep ก่อนจบ Constructor
    }

    private void checkRep() {
        assert title != null && !title.isEmpty();
        assert artist != null && !artist.isEmpty();
        assert tags != null;
        for (String t : tags) {
            assert t != null && !t.isEmpty();
        }
    }

    // ---------- observers ----------

    public String title() {
        return title;
    }

    public String artist() {
        return artist;
    }

    public List<String> tags() { 
        // Defensive Copy ขาออก (คืน ArrayList ใหม่ป้องกันภายนอกแก้ไข)
        return new ArrayList<>(tags);
    }

    // ---------- producer ----------

    /**
     * spec: คืน Song "ตัวใหม่" ที่มีแท็กเพิ่มต่อท้าย — ห้ามแก้ตัวเดิม
     * @throws IllegalArgumentException เมื่อ tag เป็น null/ว่าง
     */
    public Song withTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            throw new IllegalArgumentException("Tag error");
        }

        List<String> newTags = new ArrayList<>(this.tags);
        newTags.add(tag);

        return new Song(this.title, this.artist, newTags);
    }

    // ---------- equality ----------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return Objects.equals(title, song.title) &&
               Objects.equals(artist, song.artist) &&
               Objects.equals(tags, song.tags);
    }

    @Override 
    public int hashCode() {
        return Objects.hash(title, artist, tags);
    }

    @Override
    public String toString() {
        return title + " — " + artist + " " + tags;
    }
}