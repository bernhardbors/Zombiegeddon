package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.Player;

public class SignZone implements ITriggerAction{
    private String text;
    public SignZone(String text) { this.text = text; }

    @Override
    public String onTrigger(Player player) {
        return text; // Returnerer teksten som skal tegnes på skjermen
    }
}
