package models.player;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePlayerRequestDto {

    private Integer age;
    private String gender;
    private String login;
    private String password;
    private String screenName;

    public UpdatePlayerRequestDto() {
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public static class Builder {
        private final UpdatePlayerRequestDto dto = new UpdatePlayerRequestDto();

        public Builder age(int age) {
            dto.setAge(age);
            return this;
        }

        public Builder gender(String gender) {
            dto.setGender(gender);
            return this;
        }

        public Builder login(String login) {
            dto.setLogin(login);
            return this;
        }

        public Builder password(String password) {
            dto.setPassword(password);
            return this;
        }

        public Builder screenName(String screenName) {
            dto.setScreenName(screenName);
            return this;
        }

        public UpdatePlayerRequestDto build() {
            return dto;
        }
    }
}