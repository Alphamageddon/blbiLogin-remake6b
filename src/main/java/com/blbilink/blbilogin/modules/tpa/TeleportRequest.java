package com.blbilink.blbilogin.modules.tpa;

public class TeleportRequest {
    public String requester;
    public String target;
    public boolean here;

    public TeleportRequest(String requester, String target, boolean here) {
        this.requester = requester;
        this.target = target;
        this.here = here;
    }
}
