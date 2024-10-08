package bluescreensite.entity;

public enum UserRole {
    ROLE_ADMIN("동아리장", "관리자"),
    ROLE_EXECUTIVE("임원", "임원"),
    ROLE_MEMBER("동아리원", "일반"),
    ROLE_CLEANER("동아리원", "신입");

    private final String rank;
    private final String description;

    UserRole(String rank, String description) {
        this.rank = rank;
        this.description = description;
    }

    public String getRank() {
        return rank;
    }

    public String getDescription() {
        return description;
    }
}
