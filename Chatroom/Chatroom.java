import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        new Request();
    }
}

class Request {
    public Request() {
        Scanner scanner = new Scanner(System.in);
        boolean valid_command;
        while (true) {
            valid_command = false;
            String order = scanner.nextLine();
            String[] segregated = order.split("\\s");

            if (Patterns.create_user(order)) {
                valid_command = true;
                String user_name = segregated[2];
                int user_id = Integer.parseInt(segregated[5]);
                if (User.getUser_ids().contains(user_id)) {
                    System.out.println(ErrorType.INVALID_USER_ID.getError_type());
                } else {
                    new User(user_name, user_id);
                }
            }

            if (Patterns.join_group(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[0]);
                int group_id = Integer.parseInt(segregated[3]);
                if (ErrorType.valid_user_id(user_id)
                        && ErrorType.valid_group_id(group_id) &&
                        ErrorType.not_a_member_of_group_already(user_id, group_id) &&
                        ErrorType.valid_capacity(group_id)
                ) {
                    Group temp = null;
                    for (int i = 0; i < Group.getGroups().size(); i++) {
                        if (Group.getGroups().get(i).getGroup_id() == group_id) {
                            temp = Group.getGroups().get(i);
                        }
                    }
                    if (Group.getGroups().contains(temp)) {
                        for (int i = 0; i < User.getUsers().size(); i++) {
                            if (User.getUsers().get(i).getUser_id() == user_id) {
                                for (int j = 0; j < Group.getGroup_ids().size(); j++) {
                                    if (Group.getGroup_ids().get(j) == group_id) {
                                        Group.getGroups().get(j).addMember(User.getUsers().get(i));
                                        Group.getGroups().get(j).setNumOfMembers(1);
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println(ErrorType.INVALID_GROUP_ID.getError_type());
                    }
                }
            }

            if (Patterns.all_users(order)) {
                valid_command = true;
                User.getAllUsers();
            }

            if (Patterns.delete_account(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[2]);
                if (ErrorType.valid_user_id(user_id)) {
                    User temp = null;
                    for (int i = 0; i < User.getUser_ids().size(); i++) {
                        if (User.getUser_ids().get(i) == user_id) {
                            User.getUser_ids().remove(i);
                            temp = User.getUsers().get(i);
                            break;
                        }
                    }
                    temp.deleteGroupsOfThisAdmin(temp);
                    for (int i = 0; i < Group.getGroups().size(); i++) {
                        if (Group.getGroups().get(i).getGroup_users().contains(temp)) {
                            Group.getGroups().get(i).leave_group(Group.getGroups().get(i), user_id);
                        }
                    }
                    User.getUsers().remove(temp);
                }
            }

            if (Patterns.get_username(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[2]);
                if (ErrorType.valid_user_id(user_id)) {
                    for (int i = 0; i < User.getUsers().size(); i++) {
                        if (User.getUsers().get(i).getUser_id() == user_id) {
                            System.out.println(User.getUsers().get(i).getUser_name());
                        }
                    }
                }
            }


            if (Patterns.leave_group(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[0]);
                int group_id = Integer.parseInt(segregated[3]);
                if (ErrorType.valid_user_id(user_id)
                        && ErrorType.valid_group_id(group_id)
                        && ErrorType.is_member_of_group(group_id, user_id)) {
                }
                for (int i = 0; i < Group.getGroups().size(); i++) {
                    if (Group.getGroups().get(i).getGroup_id() == group_id) {
                        Group.getGroups().get(i).leave_group(Group.getGroups().get(i), user_id);
                        Group.getGroups().get(i).setNumOfMembers(-1);
                    }
                }
            }

            if (Patterns.change_name_to(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[0]);
                if (ErrorType.valid_user_id(user_id)) {
                    String new_user_name = segregated[4];
                    for (int i = 0; i < User.getUsers().size(); i++) {
                        if (User.getUser_ids().get(i) == user_id) {
                            User.getUsers().get(i).setUserName(new_user_name);
                        }
                    }
                }
            }


            if (Patterns.send_message_to_user(order)) {
                valid_command = true;
                int user_id_1 = Integer.parseInt(segregated[0]);
                int user_id_2 = Integer.parseInt(segregated[5]);
                int message_id = Integer.parseInt(segregated[8]);
                String content = segregated[11];
                if (ErrorType.valid_user_id(user_id_1)
                        && ErrorType.valid_user_id(user_id_2)
                        && ErrorType.message_not_sent_before(message_id)
                        && ErrorType.not_blocked(user_id_1, user_id_2)
                ) {
                    User sender = null, receiver = null;
                    for (int i = 0; i < User.getUsers().size(); i++) {
                        if (User.getUsers().get(i).getUser_id() == user_id_1)
                            sender = User.getUsers().get(i);
                        if (User.getUsers().get(i).getUser_id() == user_id_2)
                            receiver = User.getUsers().get(i);
                    }
                    new Message(message_id, content, sender, receiver);
                }
            }

            if (Patterns.send_message_to_group(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[0]);
                int group_id = Integer.parseInt(segregated[5]);
                int message_id = Integer.parseInt(segregated[8]);
                String content = segregated[11];
                if (ErrorType.valid_group_id(group_id)
                        && ErrorType.is_member_of_group(group_id, user_id)
                        && ErrorType.valid_user_id(user_id)
                        && ErrorType.message_not_sent_before(message_id)) {
                    User sender = null;
                    Group receiver = null;
                    for (int i = 0; i < User.getUsers().size(); i++) {
                        if (User.getUsers().get(i).getUser_id() == user_id)
                            sender = User.getUsers().get(i);
                    }
                    for (int i = 0; i < Group.getGroups().size(); i++) {
                        if (Group.getGroups().get(i).getGroup_id() == group_id) {
                            receiver = Group.getGroups().get(i);
                        }
                    }
                    Message message = new Message(message_id, content, sender, receiver);
                    receiver.getMessages().add(message);
                }
            }


            if (Patterns.get_all_groups(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[3]);
                if (ErrorType.valid_user_id(user_id)) {
                    for (int i = 0; i < User.getUsers().size(); i++) {
                        if (User.getUsers().get(i).getUser_id() == user_id) {
                            User.getUsers().get(i).getAllGroups(User.getUsers().get(i));
                        }
                    }
                }
            }

            if (Patterns.get_all_messages(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[3]);
                if (ErrorType.valid_user_id(user_id)) {
                    for (int i = 0; i < User.getUsers().size(); i++) {
                        if (User.getUsers().get(i).getUser_id() == user_id) {
                            User.getUsers().get(i).getAllSentMessages(User.getUsers().get(i));
                        }
                    }
                }
            }

            if (Patterns.change_group_name(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[0]);
                int group_id = Integer.parseInt(segregated[4]);
                String group_name = segregated[6];
                if (ErrorType.valid_user_id(user_id)
                        && ErrorType.valid_group_id(group_id)
                        && ErrorType.is_admin(user_id, group_id)) {
                    for (int i = 0; i < Group.getGroup_ids().size(); i++) {
                        if (Group.getGroup_ids().get(i) == group_id) {
                            if (Group.getGroups().get(i).getAdmin_id() == user_id) {
                                Group.getGroups().get(i).setGroupName(group_name);
                            }
                        }
                    }
                }
            }

            if (Patterns.add_member(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[0]);
                int new_user_id = Integer.parseInt(segregated[3]);
                int group_id = Integer.parseInt(segregated[5]);
                if (ErrorType.valid_user_id(user_id)
                        && ErrorType.valid_group_id(group_id)
                        && User.getUser_ids().contains(new_user_id)
                        && ErrorType.is_admin(user_id, group_id)
                        && ErrorType.not_a_member_of_group_already(new_user_id, group_id)
                        && ErrorType.not_blocked(user_id, new_user_id)
                        && ErrorType.valid_capacity(group_id)) {
                    Group temp = null;
                    for (int i = 0; i < Group.getGroups().size(); i++) {
                        if (Group.getGroups().get(i).getGroup_id() == group_id) {
                            temp = Group.getGroups().get(i);
                        }
                    }
                    if (Group.getGroups().contains(temp)) {
                        for (int i = 0; i < Group.getGroups().size(); i++) {
                            if (Group.getGroups().get(i).getGroup_id() == group_id) {
                                Group.getGroups().get(i).AddMember(Group.getGroups().get(i), new_user_id);
                            }
                        }
                    } else {
                        System.out.println(ErrorType.INVALID_GROUP_ID.getError_type());
                    }

                    if (User.getUser_ids().contains(user_id) && !User.getUser_ids().contains(new_user_id)) {
                        System.out.println(ErrorType.INVALID_USER_ID.getError_type());
                    }
                }
            }
            if (Patterns.block(order)) {
                valid_command = true;
                int block_konnade = Integer.parseInt(segregated[0]);
                int block_shavande = Integer.parseInt(segregated[2]);
                if (User.getUser_ids().contains(block_konnade) && User.getUser_ids().contains(block_shavande)) {
                    for (int i = 0; i < User.getUsers().size(); i++) {
                        if (User.getUsers().get(i).getUser_id() == block_konnade) {
                            User.getUsers().get(i).block(User.getUsers().get(i), block_shavande);
                        }
                    }
                } else {
                    System.out.println(ErrorType.INVALID_USER_ID.getError_type());
                }
            }

            if (Patterns.create_group(order)) {
                valid_command = true;
                int admin_id = Integer.parseInt(segregated[0]);
                String group_name = segregated[3];
                int group_id = Integer.parseInt(segregated[6]);
                int capacity = Integer.parseInt(segregated[9]);
                if (ErrorType.valid_user_id(admin_id) &&
                        ErrorType.group_id_existance(group_id)) {
                    for (int i = 0; i < User.getUsers().size(); i++) {
                        if (User.getUsers().get(i).getUser_id() == admin_id) {
                            Group group = new Group(User.getUsers().get(i), group_name, group_id, capacity);
                            User.getUsers().get(i).addToTheGroupsOfUser(group);
                        }
                    }
                }
            }

            if (Patterns.get_message_sender(order)) {
                valid_command = true;
                int message_id = Integer.parseInt(segregated[3]);
                if (ErrorType.valid_message_id(message_id)) {
                    for (int i = 0; i < Message.getMessages().size(); i++) {
                        if (Message.getMessages().get(i).getMessage_id() == message_id) {
                            Message.getMessages().get(i).getMessage_sender(Message.getMessages().get(i));
                        }
                    }
                }
            }

            if (Patterns.get_message_content(order)) {
                valid_command = true;
                int message_id = Integer.parseInt(segregated[3]);
                Message temp = null;
                if (ErrorType.valid_message_id(message_id)) {
                    for (int i = 0; i < Message.getMessages().size(); i++) {
                        if (Message.getMessages().get(i).getMessage_id() == message_id && Message.getMessages().get(i).isValid() == true) {
                            if (Message.getMessages().contains(Message.getMessages().get(i))) {
                                Message.getMessages().get(i).getMessage_content(Message.getMessages().get(i));
                                temp = Message.getMessages().get(i);
                                break;
                            }
                        }
                    }
                    if (!Message.getMessages().contains(temp) || temp.isValid() == false) {
                        System.out.println(ErrorType.INVALID_MESSAGE_ID.getError_type());
                    }
                }
            }

            if (Patterns.remove_message(order)) {
                valid_command = true;
                int user_id = Integer.parseInt(segregated[0]);
                int message_id = Integer.parseInt(segregated[3]);
                if (ErrorType.valid_message_id(message_id)
                        && ErrorType.valid_user_id(user_id)) {
                    for (int i = 0; i < Message.getMessages().size(); i++) {
                        if (Message.getMessages().get(i).getMessage_id() == message_id) {
                            if (Message.getMessages().get(i).getSender().getUser_id() == user_id) {
                                Message.getMessages().get(i).setValidity(false);
                            }
                            if (Message.getMessages().get(i).getSender().getUser_id()!=user_id){
                                System.out.println(ErrorType.INVALID_USER_ID.getError_type());
                            }
                        }
                    }
                }
            }

            if (Patterns.get_group_name(order)) {
                valid_command = true;
                int group_id = Integer.parseInt(segregated[3]);
                if (ErrorType.valid_group_id(group_id)) {
                    for (int i = 0; i < Group.getGroup_ids().size(); i++) {
                        if (Group.getGroup_ids().get(i).equals(group_id)) {
                            System.out.println(Group.getGroups().get(i).getGroup_name());
                        }
                    }
                }
            }

            if (Patterns.get_group_admin(order)) {
                valid_command = true;
                int group_id = Integer.parseInt(segregated[3]);
                if (ErrorType.valid_group_id(group_id)) {
                    for (int i = 0; i < Group.getGroup_ids().size(); i++) {
                        if (Group.getGroup_ids().get(i).equals(group_id)) {
                            System.out.println(Group.getGroups().get(i).getAdmin().getUser_name());
                        }
                    }
                }
            }

            if (Patterns.get_all_members(order)) {
                valid_command = true;
                int group_id = Integer.parseInt(segregated[3]);
                Group temp = null;
                for (int i = 0; i < Group.getGroups().size(); i++) {
                    if (Group.getGroups().get(i).getGroup_id() == group_id) {
                        temp = Group.getGroups().get(i);
                    }
                }
                if (Group.getGroups().contains(temp)) {
                    for (int i = 0; i < Group.getGroups().size(); i++) {
                        if (Group.getGroups().get(i).getGroup_id() == group_id) {
                            Group.getGroups().get(i).printGroupMembers(Group.getGroups().get(i));
                        }
                    }
                } else {
                    System.out.println(ErrorType.INVALID_GROUP_ID.getError_type());
                }
            }

            if (Patterns.get_all_group_messages(order)) {
                valid_command = true;
                int group_id = Integer.parseInt(segregated[4]);
                Group temp = null;
                for (int i = 0; i < Group.getGroups().size(); i++) {
                    if (Group.getGroups().get(i).getGroup_id() == group_id) {
                        temp = Group.getGroups().get(i);
                    }
                }
                if (Group.getGroups().contains(temp)) {
                    for (int i = 0; i < Group.getGroups().size(); i++) {
                        if (Group.getGroups().get(i).getGroup_id() == group_id) {
                            Group.getGroups().get(i).getAllGroupMessages(Group.getGroups().get(i));
                        }
                    }
                } else {
                    System.out.println(ErrorType.INVALID_GROUP_ID.getError_type());
                }
            }

            if (!valid_command && !order.equals("end")) {
                System.out.println(ErrorType.INVALID_COMMAND.getError_type());
            }
            if (order.equals("end")) {
                return;
            }

        }
    }
}

class Patterns {

    public static boolean create_user(String order) {
        Pattern dataPattern = Pattern.compile("(create user)(\\s)([^\\s]{1,99})(\\s)(with id)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean join_group(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(join group)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean all_users(String order) {
        Pattern dataPattern = Pattern.compile("(all users)(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean delete_account(String order) {
        Pattern dataPattern = Pattern.compile("(delete account)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_username(String order) {
        Pattern dataPattern = Pattern.compile("(get username)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean leave_group(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(leave group)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean change_name_to(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(change name to)(\\s)([^\\s]{1,99})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean send_message_to_user(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(send message to user)(\\s)(\\d{1,4})(\\s)(with id)(\\s)(\\d{1,4})(\\s)(with content)(\\s)([^\\s]{1,99})$");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean send_message_to_group(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(send message to group)(\\s)(\\d{1,4})(\\s)(with id)(\\s)(\\d{1,4})(\\s)(with content)(\\s)([^\\s]{1,99})$");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_all_groups(String order) {
        Pattern dataPattern = Pattern.compile("(get all groups)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_all_messages(String order) {
        Pattern dataPattern = Pattern.compile("(get all messages)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean change_group_name(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(change group name)(\\s)(\\d{1,4})(\\s)(to)(\\s)([^\\s]{1,99})$");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean add_member(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(add member)(\\s)(\\d{1,4})(\\s)(to)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean block(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(block)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean create_group(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(create group)(\\s)([^\\s]{1,99})(\\s)(with id)(\\s)(\\d{1,4})(\\s)(with capacity)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_message_sender(String order) {
        Pattern dataPattern = Pattern.compile("(get message sender)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_message_content(String order) {
        Pattern dataPattern = Pattern.compile("(get message content)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean remove_message(String order) {
        Pattern dataPattern = Pattern.compile("(\\d{1,4})(\\s)(remove message)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_group_name(String order) {
        Pattern dataPattern = Pattern.compile("(get group name)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_group_admin(String order) {
        Pattern dataPattern = Pattern.compile("(get group admin)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_all_members(String order) {
        Pattern dataPattern = Pattern.compile("(get all members)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }

    public static boolean get_all_group_messages(String order) {
        Pattern dataPattern = Pattern.compile("(get all group messages)(\\s)(\\d{1,4})(\\s{0,99})");
        Matcher matcher = dataPattern.matcher(order);
        return (matcher.matches());
    }


}

enum ErrorType {
    INVALID_USER_ID("invalid user id"),
    INVALID_GROUP_ID("invalid group id"),
    INVALID_MESSAGE_ID("invalid message id"),
    ALREADY_IN_GROUP("already in group"),
    NOT_ENOUGH_SPACE("not enough space"),
    YOU_ARE_BLOCKED("you are blocked"),
    INVALID_COMMAND("invalid command");

    private String error_type;

    ErrorType(String error_type) {
        this.error_type = error_type;
    }

    public String getError_type() {
        return error_type;
    }

    public static boolean valid_group_id(int group_id) {
        boolean no_errors_in_group = true;
        if (!Group.getGroup_ids().contains(group_id)) {
            System.out.println(ErrorType.INVALID_GROUP_ID.getError_type());
            no_errors_in_group = false;
        }
        return no_errors_in_group;
    }

    public static boolean group_id_existance(int group_id) {
        boolean no_errors_in_group = true;
        if (Group.getGroup_ids().contains(group_id)) {
            System.out.println(ErrorType.INVALID_GROUP_ID.getError_type());
            no_errors_in_group = false;
        }
        return no_errors_in_group;
    }

    public static boolean valid_message_id(int message_id) {
        boolean no_errors_in_group = true;
        if (!Message.getMessage_ids().contains(message_id)) {
            System.out.println(ErrorType.INVALID_MESSAGE_ID.getError_type());
            no_errors_in_group = false;
        }
        return no_errors_in_group;
    }

    public static boolean message_not_sent_before(int message_id) {
        boolean no_errors_in_group = true;
        if (Message.getMessage_ids().contains(message_id)) {
            no_errors_in_group = false;
            System.out.println(ErrorType.INVALID_MESSAGE_ID.getError_type());
        }
        return no_errors_in_group;
    }

    public static boolean valid_user_id(int user_id) {
        boolean no_errors_in_group = true;
        if (!User.getUser_ids().contains(user_id)) {
            System.out.println(ErrorType.INVALID_USER_ID.getError_type());
            no_errors_in_group = false;
        }
        return no_errors_in_group;
    }

    public static boolean not_a_member_of_group_already(int user_id, int group_id) {
        boolean no_errors_in_group = true;
        User temp_user = null;
        for (int i = 0; i < User.getUsers().size(); i++) {
            if (User.getUsers().get(i).getUser_id() == user_id) {
                temp_user = User.getUsers().get(i);
            }
        }
        for (int j = 0; j < Group.getGroups().size(); j++) {
            if (Group.getGroups().get(j).getGroup_id() == group_id) {
                if (Group.getGroups().get(j).getGroupMembers().contains(temp_user)) {
                    System.out.println(ErrorType.ALREADY_IN_GROUP.getError_type());
                    no_errors_in_group = false;
                    break;
                }
            }
        }
        return no_errors_in_group;
    }

    public static boolean is_member_of_group(int group_id, int user_id) {
        boolean no_errors_in_group = true;
        User temp_user = null;
        for (int i = 0; i < User.getUsers().size(); i++) {
            if (User.getUsers().get(i).getUser_id() == user_id) {
                temp_user = User.getUsers().get(i);
                no_errors_in_group = true;
            }
        }
        for (int j = 0; j < Group.getGroups().size(); j++) {
            if (Group.getGroups().get(j).getGroup_id() == group_id) {
                if (!Group.getGroups().get(j).getGroup_users().contains(temp_user)) {
                    System.out.println(ErrorType.INVALID_USER_ID.getError_type());
                    no_errors_in_group = false;
                    break;
                }
            }
        }
        return no_errors_in_group;
    }

    public static boolean valid_capacity(int group_id) {
        boolean no_errors_in_group = true;
        if (Group.getGroup_ids().contains(group_id)) {
            Group temp = null;
            for (int i = 0; i < Group.getGroups().size(); i++) {
                if (Group.getGroups().get(i).getGroup_id() == group_id) {
                    int size = Group.getGroups().get(i).getGroupMembers().size();
                    int capacity = Group.getGroups().get(i).getCapacity();
                    if (size == capacity) {
                        System.out.println(ErrorType.NOT_ENOUGH_SPACE.getError_type());
                        no_errors_in_group = false;
                        break;
                    }
                }
            }
        }
        return no_errors_in_group;
    }

    public static boolean is_admin(int user_id, int group_id) {
        boolean no_errors_in_group = true;
        if (Group.getGroup_ids().contains(group_id)) {
            Group temp = null;
            for (int i = 0; i < Group.getGroups().size(); i++) {
                if (Group.getGroups().get(i).getGroup_id() == group_id) {
                    if (Group.getGroups().get(i).getAdmin_id() != user_id) {
                        System.out.println(ErrorType.INVALID_USER_ID.getError_type());
                        no_errors_in_group = false;
                    }
                }
            }
        }
        return no_errors_in_group;
    }

    public static boolean not_blocked(int block_shavande_id, int block_konnade_id) {
        boolean no_errors_in_group = true;
        User temp_user = null;
        for (int i = 0; i < User.getUser_ids().size(); i++) {
            if (User.getUser_ids().get(i).equals(block_shavande_id)) {
                temp_user = User.getUsers().get(i);
            }
        }
        for (int i = 0; i < User.getUser_ids().size(); i++) {
            if (User.getUser_ids().get(i).equals(block_konnade_id)) {
                for (int j = 0; j < User.getUsers().get(i).getBlocked_users().size(); j++) {
                    if (User.getUsers().get(i).getBlocked_users().contains(temp_user)) {
                        System.out.println(ErrorType.YOU_ARE_BLOCKED.getError_type());
                        no_errors_in_group = false;
                    }
                }
            }
        }
        return no_errors_in_group;
    }

}

class User {

    private int user_id;
    private String user_name;
    //in 2 majmoo e ba ham bayad 1 andix dashte bashand
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Integer> user_ids = new ArrayList();
    private ArrayList<Group> all_groups_of_user = new ArrayList<>();
    private ArrayList<User> blocked_users = new ArrayList<>();
    private ArrayList<Message> sent_messages = new ArrayList<>();


    public User(String user_name, int user_id) {
        if (user_ids.contains(user_id)) {
            System.out.println("invalid user id");
        }
        if (!user_ids.contains(user_id)) {
            User.getUsers().add(this);
            user_ids.add(user_id);
            this.user_id = user_id;
            this.user_name = user_name;
        }
    }

    public static void getAllUsers() {
        ArrayList<Integer> copy_of_user_ids = user_ids;
        Collections.sort(copy_of_user_ids);
        for (int i = 0; i < copy_of_user_ids.size(); i++) {
            for (int j = 0; j < User.getUsers().size(); j++) {
                if (User.getUsers().get(j).getUser_id() == copy_of_user_ids.get(i)) {
                    System.out.println(User.getUsers().get(j).getUser_name());
                }
            }
        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static ArrayList<Integer> getUser_ids() {
        return user_ids;
    }

    public ArrayList<Group> getAll_groups_of_user() {
        return all_groups_of_user;
    }

    public void addToTheGroupsOfUser(Group group) {
        all_groups_of_user.add(group);
    }

    public void deleteGroupsOfThisAdmin(User user) {
        ArrayList<Group> groupsToOmit = new ArrayList<>();
        ArrayList<Integer> idsToOmit = new ArrayList<>();
        for (int i = 0; i < Group.getGroups().size(); i++) {
            if (Group.getGroups().get(i).getAdmin_id() == user.user_id) {
                groupsToOmit.add(Group.getGroups().get(i));
            }
        }
        for (int i = 0; i < Group.getGroups().size(); i++) {
            if (Group.getGroups().get(i).getAdmin_id() == user.user_id) {
                idsToOmit.add(Group.getGroups().get(i).getGroup_id());
            }
        }
        for (int i = 0; i < groupsToOmit.size(); i++) {
            Group.getGroups().remove(groupsToOmit.get(i));
        }
        for (int i = 0; i < idsToOmit.size(); i++) {
            Group.getGroup_ids().remove(idsToOmit.get(i));
        }
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public void getAllGroups(User user) {
        Collections.sort(this.all_groups_of_user, new SortGroupsByNumOfMembers());
        for (int i = 0; i < user.all_groups_of_user.size(); i++) {
            System.out.println(user.all_groups_of_user.get(i).getGroup_name());
        }
    }

    public ArrayList<User> getBlocked_users() {
        return blocked_users;
    }

    public void block(User block_konnade, int block_shavande_id) {
        for (int i = 0; i < User.getUsers().size(); i++) {
            if (User.getUsers().get(i).getUser_id() == block_shavande_id) {
                block_konnade.blocked_users.add(User.getUsers().get(i));
            }
        }
    }

    public void getAllSentMessages(User user) {
        ArrayList<Message> sent_messages = new ArrayList();
        for (int i = 0; i < Message.getMessages().size(); i++) {
            if (Message.getMessages().get(i).getSender().equals(user) && Message.getMessages().get(i).isValid() == true) {
                sent_messages.add(Message.getMessages().get(i));
            }
        }
        for (int i = 0; i < sent_messages.size(); i++) {
            for (int j = i + 1; j < sent_messages.size(); j++) {
                if (sent_messages.get(i).getMessage_id() > sent_messages.get(j).getMessage_id()) {
                    Collections.swap(sent_messages, i, j);
                }
            }
        }
        for (int i = 0; i < sent_messages.size(); i++) {
            System.out.print(sent_messages.get(i).getMessage_content());
            System.out.println();
        }
    }

    public void leaveGroup(Group group, User user) {
        user.all_groups_of_user.remove(group);
    }

}


class Group {

    private int admin_id;
    private String group_name;
    private int group_id;
    private int capacity;
    private User admin;

    public int getNumOfMembers() {
        return numOfMembers;
    }

    public void setNumOfMembers(int number) {
        this.numOfMembers +=number;
    }

    private int numOfMembers=0;

    public static ArrayList<Integer> getGroup_ids() {
        return group_ids;
    }


    public ArrayList<Message> getMessages() {
        return messages;
    }

    //in 2 majmoo e ba ham bayad 1 andix dashte bashand
    private static ArrayList<Group> groups = new ArrayList<>();
    private static ArrayList<Integer> group_ids = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();

    public Group(User admin, String group_name, int group_id, int capacity) {
        Group.groups.add(this);
        group_ids.add(group_id);
        this.admin = admin;
        this.admin_id = admin.getUser_id();
        this.group_name = group_name;
        this.group_id = group_id;
        this.capacity = capacity;
        this.users.add(admin);
        this.numOfMembers++;
    }

    public ArrayList<User> getGroup_users() {
        return users;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public String getGroup_name() {
        return group_name;
    }


    public ArrayList<User> getGroupMembers() {
        ArrayList<User> copy_of_group_users = this.users;
        swap(copy_of_group_users);
        return copy_of_group_users;
    }


    public void printGroupMembers(Group group) {
        ArrayList<User> copy_of_group_users = new ArrayList<>();
        for (int i = 0; i < group.users.size(); i++) {
            if (User.getUsers().contains(group.users.get(i))) {
                copy_of_group_users.add(group.users.get(i));
            }
        }
        swap(copy_of_group_users);
        for (int i = 0; i < copy_of_group_users.size(); i++) {
            System.out.println(copy_of_group_users.get(i).getUser_name());
        }
    }

    public void swap(ArrayList<User> copy_of_group_users) {
        for (int i = 0; i < copy_of_group_users.size(); i++) {
            for (int j = i + 1; j < copy_of_group_users.size(); j++) {
                if (copy_of_group_users.get(i).getUser_id() > copy_of_group_users.get(j).getUser_id()) {
                    Collections.swap(copy_of_group_users, i, j);
                }
            }
        }
    }

    public void getAllGroupMessages(Group group) {
        ArrayList<Message> group_messages = new ArrayList<>();
        for (int i = 0; i < group.messages.size(); i++) {
            if (group.messages.get(i).isValid() == true) {
                group_messages.add(group.messages.get(i));
            }
        }

        for (int i = 0; i < group_messages.size(); i++) {
            for (int j = i + 1; j < group_messages.size(); j++) {
                if (group_messages.get(i).getMessage_id() > group_messages.get(j).getMessage_id()) {
                    Collections.swap(group_messages, i, j);
                }
            }
        }
        for (int i = 0; i < group_messages.size(); i++) {
            System.out.println(group_messages.get(i).getMessage_content());
        }
    }


    public void addMember(User user) {
        users.add(user);
        user.addToTheGroupsOfUser(this);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getGroup_id() {
        return group_id;
    }

    public User getAdmin() {
        return admin;
    }

    public static ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public void AddMember(Group group, int new_user_id) {
        User new_user = null;
        for (int i = 0; i < User.getUser_ids().size(); i++) {
            if (User.getUser_ids().get(i) == new_user_id) {
                new_user = User.getUsers().get(i);
            }
        }
        group.users.add(new_user);
        new_user.getAll_groups_of_user().add(group);

    }

    public void leave_group(Group group, int user_id) {
        User temp = null;
        for (int i = 0; i < User.getUsers().size(); i++) {
            if (User.getUsers().get(i).getUser_id() == user_id) {
                temp = User.getUsers().get(i);
                User.getUsers().get(i).leaveGroup(group, User.getUsers().get(i));
            }
        }
        group.users.remove(temp);
    }
}

class Message {
    //in 2 majmoo e ba ham bayad 1 andix dashte bashand
    private static ArrayList<Message> messages = new ArrayList<>();
    private static ArrayList<Integer> message_ids = new ArrayList<>();
    private User sender;
    private User user_receiver;
    private Group group_receiver;
    private String message_content;
    private int message_id;

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    private boolean validity = true;

    public static ArrayList<Message> getMessages() {
        return messages;
    }

    public static ArrayList<Integer> getMessage_ids() {
        return message_ids;
    }

    public User getSender() {
        return sender;
    }

    public boolean isValid() {
        return validity;
    }

    public Message(int message_id, String content, User sender, User receiver) {
        this.message_content = content;
        this.message_id = message_id;
        messages.add(this);
        message_ids.add(message_id);
        this.sender = sender;
        this.user_receiver = receiver;
    }

    public Message(int message_id, String content, User sender, Group receiver) {
        this.message_content = content;
        this.message_id = message_id;
        messages.add(this);
        message_ids.add(message_id);
        this.sender = sender;
        this.group_receiver = receiver;
    }

    public int getMessage_id() {
        return message_id;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void getMessage_content(Message message) {
        for (int i = 0; i < Message.messages.size(); i++) {
            if (Message.messages.get(i).equals(message)) {
                System.out.println(Message.messages.get(i).message_content);
            }
        }
    }


    public void getMessage_sender(Message message) {
        for (int i = 0; i < Message.messages.size(); i++) {
            if (Message.messages.get(i).equals(message) && User.getUsers().contains(Message.messages.get(i).sender)) {
                System.out.println(Message.messages.get(i).sender.getUser_name());
            }
        }

    }
}


class SortGroupsByNumOfMembers implements Comparator<Group> {
    // Used for sorting in ascending order
    public int compare(Group a, Group b) {
        return a.getNumOfMembers() - b.getNumOfMembers();
    }
}
