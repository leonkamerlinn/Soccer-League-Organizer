import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.teamtreehouse.model.TeamBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class LeagueManager {
    private static final String CREATE_TEAM = "Create";
    private static final String ADD_PLAYER = "Add";
    private static final String REMOVE_PLAYERS = "Remove";
    private static final String HEIGHT_REPORT = "HR";
    private static final String BALANCE_REPORT = "ER";
    private static final String PRINT_ROSTER = "Roster";
    private static final String QUIT = "Quit";
    private static final String EXPERIENCE_LEVEL = "Experience";
    private static final List<Player> mPlayerList = new ArrayList<>();

    private static final Map<String, String> mMenu = new HashMap<>();
    private static final Map<String, TeamBuilder> mTeams = new TreeMap<>();
    private static TeamBuilder mTeamBuilder = null;
    private static String mInput = "";

    public static void main(String[] args) {
        Player[] players = Players.load();
        mPlayerList.addAll(Arrays.asList(players));
        System.out.printf("There are currently %d registered players.%n", players.length);

        mMenu.put(CREATE_TEAM ,"Create a new team");
        mMenu.put(ADD_PLAYER, "Add players to a team");
        mMenu.put(REMOVE_PLAYERS, "Remove players from a team");
        mMenu.put(HEIGHT_REPORT, "Print height report");
        mMenu.put(BALANCE_REPORT, "League Balance Report");
        mMenu.put(PRINT_ROSTER, "Print roster");
        mMenu.put(EXPERIENCE_LEVEL, "Average experience level ");
        mMenu.put(QUIT, "Exit the program");



        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n******************************** \n");

            for(Map.Entry<String, String>choice: mMenu.entrySet()){
                System.out.printf("%s - %s %n", choice.getKey(), choice.getValue());
            }

            System.out.println("\n********************************");
            mInput = scanner.nextLine();

            switch (mInput) {
                case CREATE_TEAM:
                    mTeamBuilder = createTeam();
                    break;

                case ADD_PLAYER:
                    TeamBuilder teamBuilder = chooseTeam();
                    if (teamBuilder != null) {
                        Player player = choosePlayerFromList();
                        if (player != null) {
                            if (!teamBuilder.isFull()) {
                                teamBuilder.addPlayer(player);
                                mPlayerList.remove(player);
                            }
                        }


                    }

                    break;

                case REMOVE_PLAYERS:
                    TeamBuilder tmb = chooseTeam();
                    if (tmb != null) {
                        Player player = choosePlayerFromTeam(tmb.build());
                        if (player != null) {
                            mPlayerList.add(player);
                            tmb.removePlayer(player);
                        }

                    }
                    break;

                case HEIGHT_REPORT:

                    try {
                        chooseTeam()
                                .build()
                                .heightReport();
                    } catch (NullPointerException e) {

                    }

                    break;

                case BALANCE_REPORT:
                    try {


                        for (Map.Entry<String, TeamBuilder> teamBuilderEntry: mTeams.entrySet()) {
                            System.out.println("\n********************************\n");
                            String teamName = teamBuilderEntry.getKey();
                            System.out.printf("%s \n", teamName);
                            Team team = teamBuilderEntry.getValue().build();
                            team.balanceReport();
                        }

                    } catch (NullPointerException e) {

                    }


                    Map<Integer, Map<String, Integer>> heights = extraBalanceReport(players);

                    for (Map.Entry<Integer, Map<String, Integer>> height: heights.entrySet()) {
                        int h = height.getKey();
                        System.out.printf("\n%d Inches -> ", h);
                        for (Map.Entry<String, Integer> teamHeightCount: height.getValue().entrySet()) {
                            String teamName = teamHeightCount.getKey();
                            int heightCount = teamHeightCount.getValue();
                            System.out.printf("Team Name: %s, Number of players: %d, ", teamName, heightCount);
                        }
                        System.out.printf("\n");
                    }



                    break;

                case PRINT_ROSTER:
                    try {
                        chooseTeam()
                                .build()
                                .printRoster();
                    } catch (NullPointerException e) {

                    }

                    break;

                case EXPERIENCE_LEVEL:

                    System.out.println("\n********************************");

                    Map<String, Map<Integer, Integer>> expertiseLevel = expertiseLevel();
                    expertiseLevel.forEach((teamName, value) -> {
                        System.out.printf("********** %s ***********\n", teamName);

                        Map.Entry<Integer, Integer> dd = value.entrySet().iterator().next();
                        Integer numberOfExperiencedPlayers = dd.getKey();
                        Integer numberOfPlayers = dd.getValue();
                        float percent =  (float)numberOfExperiencedPlayers/(float)numberOfPlayers;
                        System.out.printf("%d/%d -> %s%%\n", numberOfExperiencedPlayers, numberOfPlayers, Math.floor(percent*100));
                    });

                    System.out.println("********************************\n");

                    break;

                default:
                    if (!mInput.equals(QUIT)) {
                        System.out.println("The wrong choice, try again.");
                    }

                    break;
            }
        } while (!mInput.equals(QUIT));




    }


    private static TeamBuilder createTeam() {


        Scanner scanner = new Scanner(System.in);
        System.out.println("\n********************************");
        System.out.println("********** Create team ***********");
        System.out.println("********************************\n");

        if (mTeams.size() >= 3) {
            System.out.println("Maximum number of teams is 3");
            return null;
        }

        System.out.println("Please enter a team name: ");
        String teamName = scanner.nextLine();
        System.out.println("Please enter a coach name: ");
        String coach = scanner.nextLine();

        TeamBuilder teamBuilder = Team.Builder(teamName, coach);
        mTeams.put(teamName, teamBuilder);
        return teamBuilder;
    }

    private static TeamBuilder chooseTeam() {
        System.out.println("\n********************************");
        System.out.println("********* Choose team ************");
        System.out.println("********************************\n");

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n******************************** \n");

        if (mTeams.size() <= 0) {
            System.out.println("You don't have any team");
            return null;
        }

        if (mTeams.size() == 1) {
            Map.Entry<String, TeamBuilder> entry = mTeams.entrySet().iterator().next();
            return entry.getValue();
        }
        Map<Integer, Team> teamMap = new HashMap<>();
        int i = 0;
        for(Map.Entry<String, TeamBuilder>choice: mTeams.entrySet()){
            i++;
            String teamName = choice.getKey();
            Team team = choice.getValue().build();
            System.out.printf("%d -> %s - %s %n", i, teamName, team.toString());
            teamMap.put(i, team);
        }

        System.out.println("\n********************************\n");
        String yourChoose = scanner.nextLine();


        try {
            int index = Integer.parseInt(yourChoose);

            if (teamMap.containsKey(index)) {
                return teamMap.get(index);
            } else {
                System.out.println("The wrong choice, try again.");
            }

        } catch (Exception e) {

        }


        return null;
    }

    private static Player choosePlayerFromList() {
        System.out.println("\n********************************");
        System.out.println("******* Choose player ************");
        System.out.println("********************************\n");

        Collections.sort(mPlayerList);
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n******************************** \n");

        if (mPlayerList.size() == 0) {
            System.out.println("No more players");
            return null;
        }

        for(int i = 0; i < mPlayerList.size(); i++){
            Player player = mPlayerList.get(i);
            System.out.printf("%d -> %s\n", i+1, player.toString());
        }

        System.out.println("\n********************************\n");
        String yourChoose = scanner.nextLine();

        try {
           return mPlayerList.get(Integer.parseInt(yourChoose)-1);
        } catch (Exception e) {

        }
        return null;
    }

    private static Player choosePlayerFromTeam(Team team) {
        System.out.println("\n********************************");
        System.out.println("***** Choose team player *********");
        System.out.println("********************************\n");

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n******************************** \n");

        for(int i = 0; i < team.getPlayers().size(); i++){
            Player player = new ArrayList<>(team.getPlayers()).get(i);
            System.out.printf("%d -> %s\n", i+1, player.toString());
        }

        System.out.println("\n********************************\n");
        String yourChoose = scanner.nextLine();

        try {
            return new ArrayList<>(team.getPlayers()).get(Integer.parseInt(yourChoose)-1);
        } catch (Exception e) {

        }


        return null;
    }


    private static Map<Integer, Map<String, Integer>> extraBalanceReport(Player[] players) {
        Set<Integer> uniqueHeights = Arrays.stream(players).map(Player::getHeightInInches).collect(Collectors.toCollection(TreeSet::new));

        Map<Integer, Map<String, Integer>> collection = new TreeMap<>();

        for (int height: uniqueHeights) {

            Map<String, Integer> teamheights = new TreeMap<>();

            for(Map.Entry<String, TeamBuilder> entry: mTeams.entrySet()) {
                String teamName = entry.getKey();
                Team team = entry.getValue().build();

                teamheights.put(teamName, team.getPlayers().stream().filter(player -> player.getHeightInInches() == height).collect(Collectors.toList()).size());
                collection.put(height, teamheights);
            }
        }

        return collection;
    }


    private static Map<String, Map<Integer, Integer>> expertiseLevel() {
        Map<String, Map<Integer, Integer>> mapMap = new TreeMap<>();

        for(Map.Entry<String, TeamBuilder> teamBuilderEntry: mTeams.entrySet()) {
            String teamName = teamBuilderEntry.getKey();
            Team team = teamBuilderEntry.getValue().build();

            int numberOfExperincedPlayers = team.getPlayers()
                    .stream()
                    .filter(Player::isPreviousExperience)
                    .collect(Collectors.toList())
                    .size();

            Map<Integer, Integer> integerMap = new HashMap<>();
            integerMap.put(numberOfExperincedPlayers, team.getPlayers().size());

            mapMap.put(teamName, integerMap);

        }


        return mapMap;
    }



}