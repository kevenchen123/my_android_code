package com.keven.retrofit;

import java.util.Date;
import java.util.List;

/**
 * Created by Miroslaw Stanek on 22.04.15.
 */
public class UserResponse {
    public String login;
    public String id;
    public String avatar_url;
    public String gravatar_id;
    public String url;
    public String html_url;
    public String followers_url;
    public String following_url;
    public String gists_url;
    public String starred_url;
    public String subscriptions_url;
    public String organizations_url;
    public String repos_url;
    public String events_url;
    public String received_events_url;
    public String type;
    public String name;
    public String blog;
    public String location;
    public String email;
    public int public_repos;
    public int public_gists;
    public int followers;
    public int following;
    public Date created_at;
    public Date updated_at;


    public List<Contact> contact;

    public static class Contact {
        public Phone phone;

        public static class Phone {
            public List<String> number;

            @Override
            public String toString() {
                return "Phone{" +
                        "number=" + number +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "phone=" + phone +
                    '}';
        }
    }

}