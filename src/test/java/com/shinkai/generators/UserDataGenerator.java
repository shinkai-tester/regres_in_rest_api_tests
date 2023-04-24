package com.shinkai.generators;

import com.github.javafaker.Faker;

public class UserDataGenerator {
    static final Faker faker = new Faker();

    public String getEmail() {
        return faker.internet().safeEmailAddress();
    }

    public String getUsername() {
        return faker.name().username();
    }

    public String getFullName() {
        return faker.name().fullName();
    }

    public String getJob() {
        return faker.job().title();
    }

    public String getAvatarLink() {
        return faker.internet().avatar();
    }

    public String getPassword() {
        return faker.internet().password();
    }
}
