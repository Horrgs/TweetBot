package org.horrgs.twitterbot.commands;

import com.google.gson.*;
import org.horrgs.twitterbot.HorrgsTwitter;
import org.json.JSONArray;
import org.json.JSONException;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.*;

/**
 * Created by Horrgs on 10/17/2015.
 */
public class Permission implements SubCommand {
    @Override
    public void onCommand(Status status, String[] args) {
        StatusUpdate statusUpdate = new StatusUpdate("");
        long r1 = status.getId();
        try {
            if(args.length == 4) {
                String aor = args[1], perm = args[2], user = args[3];
                //command - add/remove - permission - user.
                Gson gson = new Gson();
                JsonObject jsonObject = null;
                JsonParser jsonParser = new JsonParser();
                try {
                    Object obj = jsonParser.parse(new FileReader("permissions.json"));
                    jsonObject = (JsonObject) obj;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                int lineNumber = 0;
                boolean alreadyHas = false;
                User user1 = HorrgsTwitter.twitter.showUser(user);
                for(int x = 0; x < jsonObject.get(perm).getAsJsonArray().size(); x++) {
                    if(jsonObject.get(perm).getAsJsonArray().get(x).equals(String.valueOf(user1.getId()))) {
                        lineNumber = x;
                        alreadyHas = true;
                        break;
                    }
                }
                long idToAdd = HorrgsTwitter.twitter.showUser(user).getId();
                PermissionFormat permissionFormat = new PermissionFormat(idToAdd);
                JsonElement jsonElement = gson.toJsonTree(permissionFormat);
                if(aor.equals("add")) {
                    if(alreadyHas) {
                        statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " user \"@" + user + "\" already has that permission.");
                        statusUpdate.setInReplyToStatusId(r1);
                        HorrgsTwitter.twitter.updateStatus(statusUpdate);
                        return;
                    }
                    jsonObject.get(perm).getAsJsonArray().add(jsonElement);
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("permissions.json"));
                    bufferedWriter.write(jsonObject.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " Successfully added permission \"" +
                            perm + "\" from @" + user1.getScreenName());
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                } else if(aor.equals("remove")) {
                    JSONArray list = new JSONArray();
                    JSONArray jsonArray = new JSONArray(jsonObject.get(perm).toString());
                    int a = jsonArray.length();
                    if (jsonArray != null) {
                        for (int x = 0; x < a; x++) {
                            if(x != lineNumber) {
                                list.put(jsonArray.get(x));
                            }
                        }
                    }
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("permissions.json"));
                    jsonObject.remove(perm);
                    JsonElement element = gson.fromJson(list.toString(), JsonElement.class);
                    jsonObject.add(perm, element);
                    bufferedWriter.write(jsonObject.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " Successfully removed permission \"" +
                            perm + "\" from @" + user1.getScreenName());
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);

                } else {
                    statusUpdate = new StatusUpdate("@" + status.getUser().getScreenName() + " you gave invalid arguments.\n Syntax: " + "%perm +/- <perm> <user>");
                    statusUpdate.setInReplyToStatusId(r1);
                    HorrgsTwitter.twitter.updateStatus(statusUpdate);
                }
            } else {
                //TODO: tell them invalid syntax.
            }
        } catch (TwitterException | IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "Add or remove a permission from a specified user.";
    }

    @Override
    public String getPermission() {
        return "tweetbot.admin.permission";
    }

    private class PermissionFormat {
        public long id;

        public PermissionFormat(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}