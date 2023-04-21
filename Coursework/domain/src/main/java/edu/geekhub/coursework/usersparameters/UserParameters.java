package edu.geekhub.coursework.usersparameters;

public class UserParameters {
    private int userId;
    private int age;
    private int weight;
    private int height;
    private Gender gender;
    private ActivityLevel activityLevel;
    private BodyType bodyType;
    private Aim aim;

    public UserParameters(int userId,
                          int age,
                          int weight,
                          int height,
                          Gender gender,
                          ActivityLevel activityLevel,
                          BodyType bodyType,
                          Aim aim) {
        this.userId = userId;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.activityLevel = activityLevel;
        this.bodyType = bodyType;
        this.aim = aim;
    }

    public UserParameters() {
        this(-1, 0, 0, 0, null, null, null, null);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Aim getAim() {
        return aim;
    }

    public void setAim(Aim aim) {
        this.aim = aim;
    }

    @Override
    public String toString() {
        return "UserParameters{"
               + "userId=" + userId
               + ", age=" + age
               + ", weight=" + weight
               + ", height=" + height
               + ", gender=" + gender
               + ", activityLevel=" + activityLevel
               + ", bodyType=" + bodyType
               + ", aim=" + aim
               + '}';
    }
}
