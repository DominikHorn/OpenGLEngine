package com.openglengine.eventsystem.defaultcomponentevents;

import com.openglengine.eventsystem.*;

/**
 * This event will be received by the CameraComponentFollowEntity component and will make sure that the camera behaves
 * in intuitive ways. Make sure to send this event in any physics component that moves the player
 * 
 * @author Dominik
 *
 */
public class FollowedEntityMovedEvent extends ComponentEvent {

}
