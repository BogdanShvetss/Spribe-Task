package models.player;

import java.util.Objects;

public class PlayerShortDto {

    private Integer id;
    private String screenName;
    private String gender;
    private Integer age;

    public PlayerShortDto() {
    }

    public PlayerShortDto(Integer id, String screenName, String gender, Integer age) {
        this.id = id;
        this.screenName = screenName;
        this.gender = gender;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerShortDto that)) return false;
        return Objects.equals(id, that.id) &&
                Objects.equals(screenName, that.screenName) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, screenName, gender, age);
    }

    @Override
    public String toString() {
        return "PlayerShortDto{" +
                "id=" + id +
                ", screenName='" + screenName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }
}