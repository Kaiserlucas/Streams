package schiffeVersenken;

import schiffeVersenken.protocolBinding.StreamBindingSender;

import java.io.IOException;

public class Sender implements SchiffeVersenkenSender {
    private final StreamBindingSender binding;

    public Sender(StreamBindingSender binding) {
        this.binding = binding;
    }

    @Override
    public void sendReihenfolgeWuerfeln(int random) throws IOException, StatusException {
        binding.sendReihenfolgeWuerfeln(random);
    }

    @Override
    public void sendKoordinate(int zeile, int spalte) throws IOException, StatusException, SchiffeVersenkenException {
        binding.sendKoordinate(zeile, spalte);
    }

    @Override
    public void sendKapitulation() throws IOException, StatusException {
        binding.sendKapitulation();
    }

    @Override
    public void sendBestaetigen(int status) throws IOException, StatusException, SchiffeVersenkenException {
        binding.sendBestaetigen(status);
    }
}
