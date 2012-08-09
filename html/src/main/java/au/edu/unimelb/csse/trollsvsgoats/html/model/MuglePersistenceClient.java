package au.edu.unimelb.csse.trollsvsgoats.html.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import au.edu.unimelb.csse.mugle.client.api.Services;
import au.edu.unimelb.csse.mugle.client.multiplayer.api.MultiServices;
import au.edu.unimelb.csse.mugle.shared.api.KeyError;
import au.edu.unimelb.csse.mugle.shared.multiplayer.api.GameState;
import au.edu.unimelb.csse.trollsvsgoats.core.model.Badge;
import au.edu.unimelb.csse.trollsvsgoats.core.model.GameModel;
import au.edu.unimelb.csse.trollsvsgoats.core.model.PersistenceClient;

public class MuglePersistenceClient implements PersistenceClient {

    private static final String gameToken = "aecc949c-ab95-4ade-93cf-ebb414a84ff4";
    // private static final String gameToken =
    // "d63700f5-f095-49a4-8e39-41555ee3d0ec";
    private String instanceToken;
    private boolean isSoundEnabled = true;
    private int screenWidth;
    private float movementTime;

    @Override
    public void persist(final GameModel model) {
        if (model.isLevelDataDirty()) {
            Services.keyvalue.put(gameToken, "maxCompletedLevel",
                    model.maxCompletedLevel(), new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Error while persisting game: "
                                    + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                        }
                    });

            Services.keyvalue.put(gameToken, "levelScores", model.scores(),
                    new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Error while persisting game: "
                                    + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                        }
                    });
        }
        if (this.isSoundEnabled != model.isSoundEnabled())
            Services.keyvalue.put(gameToken, "soundEnabled",
                    model.isSoundEnabled(), new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Error while persisting game: "
                                    + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            isSoundEnabled = model.isSoundEnabled();
                        }
                    });
        if (this.screenWidth != model.screenWidth())
            Services.keyvalue.put(gameToken, "screenWidth",
                    model.screenWidth(), new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Error while persisting game: "
                                    + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            screenWidth = model.screenWidth();
                        }
                    });

        if (this.movementTime != model.movementTime())
            Services.keyvalue.put(gameToken, "movementTime",
                    model.movementTime(), new AsyncCallback<Void>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Error while persisting game: "
                                    + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Void result) {
                            movementTime = model.movementTime();
                        }
                    });
    }

    @Override
    public void populate(final GameModel model) {
        Services.keyvalue.get(gameToken, "maxCompletedLevel",
                new AsyncCallback<Serializable>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (!(caught instanceof KeyError))
                            Window.alert("Error while retrieving max completed level: "
                                    + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Serializable result) {
                        model.setMaxCompletedLevel((Integer) result);
                    }
                });

        Services.keyvalue.get(gameToken, "levelScores",
                new AsyncCallback<Serializable>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (!(caught instanceof KeyError))
                            Window.alert("Error while retrieving level scores: "
                                    + caught.getMessage());
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(Serializable result) {
                        model.setLevelScores((HashMap<Integer, Integer>) result);
                    }
                });

        Services.keyvalue.get(gameToken, "soundEnabled",
                new AsyncCallback<Serializable>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (!(caught instanceof KeyError))
                            Window.alert("Error while retrieving sound status: "
                                    + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Serializable result) {
                        model.setSoundEnabled(isSoundEnabled = (Boolean) result);
                    }
                });

        Services.keyvalue.get(gameToken, "screenWidth",
                new AsyncCallback<Serializable>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (!(caught instanceof KeyError))
                            Window.alert("Error while retrieving screen width: "
                                    + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Serializable result) {
                        model.setScreenWidth((Integer) result);
                    }
                });

        Services.keyvalue.get(gameToken, "movementTime",
                new AsyncCallback<Serializable>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (!(caught instanceof KeyError))
                            Window.alert("Error while retrieving movementTime: "
                                    + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Serializable result) {
                        model.setMovementTime((Float) result);
                    }
                });

        Services.keyvalue.get(gameToken, "instanceToken",
                new AsyncCallback<Serializable>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (!(caught instanceof KeyError))
                            Window.alert("Error while retrieving instance token: "
                                    + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Serializable result) {
                        instanceToken = (String) result;
                    }
                });

        Services.badges.getBadgeNames(gameToken, new AsyncCallback<String[]>() {

            @Override
            public void onSuccess(String[] result) {
                for (final String badge : result) {
                    Services.badges.isAchieved(gameToken, badge,
                            new AsyncCallback<Boolean>() {

                                @Override
                                public void onSuccess(Boolean result) {
                                    if (result)
                                        model.setBadgeAchieved(badge);
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    Window.alert("Error while retriving achievement status of "
                                            + badge
                                            + " from server: "
                                            + caught.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error while retriving badge names from server: "
                        + caught.getMessage());
            }
        });
    }

    private void storeInstanceToken() {
        Services.keyvalue.put(gameToken, "instanceToken", instanceToken,
                new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error while storing instance token: "
                                + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                    }
                });
    }

    @Override
    public void getUserName(final Callback<String> callBack) {
        Services.user.getUserNickName(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                callBack.onFailure(caught);
            }
        });
    }

    @Override
    public void achieveBadge(final Badge badge) {
        Services.badges.setAchieved(gameToken, badge.name(),
                new AsyncCallback<Void>() {

                    @Override
                    public void onSuccess(Void result) {
                        badge.setAchieved();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error while setting badge "
                                + badge.displayName()
                                + " as achieved on the server:"
                                + caught.getMessage());
                    }
                });
    }

    @Override
    public void logTrollsDeployment(final int level, final List<String> lanes) {
        if (instanceToken == null) {
            MultiServices.multigame.createGameInstance(gameToken, 1, false,
                    new AsyncCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            instanceToken = result;
                            storeInstanceToken();
                            logTrollsDeployment(level, lanes);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Error while creating game instance: "
                                    + caught.getMessage());
                        }
                    });
        } else {
            MultiServices.multigame.joinGame(instanceToken,
                    new AsyncCallback<String[]>() {

                        @Override
                        public void onSuccess(String[] result) {
                            GameState state = new GameState();
                            state.addList("Trolls Deployment", lanes);
                            state.addAttribute("Level", String.valueOf(level));
                            MultiServices.logging.log(instanceToken, state,
                                    false, new AsyncCallback<Void>() {

                                        @Override
                                        public void onSuccess(Void result) {
                                        }

                                        @Override
                                        public void onFailure(Throwable caught) {
                                            Window.alert("Error while logging trolls deployment: "
                                                    + caught.getMessage());
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Error while joining game instance: "
                                    + caught.getMessage());
                        }
                    });

        }
    }
}
