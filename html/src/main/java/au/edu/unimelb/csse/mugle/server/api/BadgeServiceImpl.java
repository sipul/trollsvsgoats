package au.edu.unimelb.csse.mugle.server.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import au.edu.unimelb.csse.mugle.client.api.BadgeService;
import au.edu.unimelb.csse.mugle.shared.api.Badge;
import au.edu.unimelb.csse.mugle.shared.api.BadgeError;
import au.edu.unimelb.csse.mugle.shared.api.GameTokenError;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BadgeServiceImpl extends RemoteServiceServlet implements
        BadgeService {
    /** Test implementation: Hard-code some badges for the test game. */
    private static Map<String, Badge> static_badge_map;
    /**
     * Test implementation: Store the state in memory. Maps game tokens onto a
     * map from badge names to user-badges.
     */
    private static Map<String, Map<String, UserBadge>> game_badge_map = new HashMap<String, Map<String, UserBadge>>();

    static {
        static_badge_map = new HashMap<String, Badge>();

        static_badge_map.put("firstBlood", new Badge("firstBlood",
                "First Blood", "First Goat eaten", 0));
        static_badge_map.put("momentExpert", new Badge("momentExpert",
                "Moment Expert", "First level that doesn't leave 0 N/m", 0));
    }

    @Override
    public String[] getBadgeNames(String gameToken) throws GameTokenError {
        GameToken.validate_format(gameToken);
        Set<String> nameSet = static_badge_map.keySet();
        return nameSet.toArray(new String[nameSet.size()]);
    }

    private Badge get_badge(String name) throws BadgeError {
        Badge b = static_badge_map.get(name);
        if (b == null)
            throw new BadgeError("No badge named " + name);
        return b;
    }

    @Override
    public Badge getBadgeInfo(String gameToken, String name)
            throws GameTokenError, BadgeError {
        GameToken.validate_format(gameToken);
        return this.get_badge(name);
    }

    private UserBadge get_userbadge(String gameToken, String name)
            throws GameTokenError, BadgeError {
        GameToken.validate_format(gameToken);
        this.get_badge(name); // Check that this is really a badge
        Map<String, UserBadge> badge_map = game_badge_map.get(gameToken);
        if (badge_map == null) {
            badge_map = new HashMap<String, UserBadge>();
            game_badge_map.put(gameToken, badge_map);
        }
        UserBadge badge = badge_map.get(name);
        if (badge == null) {
            badge = new UserBadge();
            badge_map.put(name, badge);
        }
        return badge;
    }

    @Override
    public void setAchieved(String gameToken, String name)
            throws GameTokenError, BadgeError {
        this.get_userbadge(gameToken, name).achieved = true;
    }

    @Override
    public boolean incrementProgress(String gameToken, String name, int amount)
            throws GameTokenError, BadgeError {
        UserBadge ub = this.get_userbadge(gameToken, name);
        Badge b = this.get_badge(name);
        if (amount < 0)
            throw new BadgeError(
                    "incrementProgress called with negative amount");
        int maxProgress = b.getMaxProgress();
        if (maxProgress == 0)
            throw new BadgeError(
                    "incrementProgress called on non-progress badge "
                            + b.getName());
        int new_progress = ub.progress + amount;
        if (new_progress > maxProgress)
            new_progress = maxProgress;
        ub.progress = new_progress;
        if (new_progress == maxProgress) {
            ub.achieved = true;
            return true;
        } else
            return false;
    }

    @Override
    public boolean incrementProgress(String gameToken, String name)
            throws GameTokenError, BadgeError {
        return this.incrementProgress(gameToken, name, 1);
    }

    @Override
    public boolean isAchieved(String gameToken, String name)
            throws GameTokenError, BadgeError {
        return this.get_userbadge(gameToken, name).achieved;
    }

    @Override
    public int getProgress(String gameToken, String name)
            throws GameTokenError, BadgeError {
        return this.get_userbadge(gameToken, name).progress;
    }
}
