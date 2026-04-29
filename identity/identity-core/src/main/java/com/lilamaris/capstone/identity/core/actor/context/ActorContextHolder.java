package com.lilamaris.capstone.identity.core.actor.context;

import com.lilamaris.capstone.identity.core.actor.Actor;

public interface ActorContextHolder {
    Actor getActor();

    void setActor(Actor actor);

    void clear();
}
