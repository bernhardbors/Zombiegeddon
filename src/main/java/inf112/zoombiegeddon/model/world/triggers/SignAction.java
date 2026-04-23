package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.IPlayer;

public class SignAction implements ITriggerAction{
    private String text;

    public SignAction(String text) { this.text = text; }

    @Override
    public String onTrigger(IPlayer player) {
        return text; // Returnerer teksten som skal tegnes på skjermen
    }
}
