package models.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerDto {
    private Integer id;
    private String login;
    private String password;
    private Role role;
    private String screenName;
    private int age;
    private String gender;

    public PlayerDto() {
    }

    public PlayerDto(Integer id, String login, String password, Role role, String screenName, int age, String gender) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.screenName = screenName;
        this.age = age;
        this.gender = gender;
    }

    public Map<String, Object> buildCreateQuery() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("age", age);
        queryParams.put("gender", gender);
        queryParams.put("login", login);
        queryParams.put("password", password);
        queryParams.put("role", role != null ? role.getValue() : null);
        queryParams.put("screenName", screenName);
        queryParams.values().removeIf(Objects::isNull);

        return queryParams;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerDto)) return false;
        PlayerDto player = (PlayerDto) o;
        return age == player.age &&
                Objects.equals(id, player.id) &&
                Objects.equals(login, player.login) &&
                Objects.equals(password, player.password) &&
                role == player.role &&
                Objects.equals(screenName, player.screenName) &&
                gender == player.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, screenName, age, gender);
    }

    @Override
    public String toString() {
        return "PlayerDto{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", screenName='" + screenName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }

    public static class Builder {
        private String login;
        private String password;
        private Role role;
        private String screenName;
        private int age;
        private String gender;

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder screenName(String screenName) {
            this.screenName = screenName;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public PlayerDto build() {
            return new PlayerDto(null, login, password, role, screenName, age, gender);
        }
    }
}