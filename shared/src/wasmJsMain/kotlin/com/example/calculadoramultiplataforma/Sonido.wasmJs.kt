package com.example.calculadoramultiplataforma

private fun beepNavegador() {
    js("""
        (() => {
            const audio = new AudioContext();
            const oscillator = audio.createOscillator();
            const gain = audio.createGain();

            oscillator.type = "triangle";
            oscillator.frequency.value = 600;

            gain.gain.setValueAtTime(0.15, audio.currentTime);
            gain.gain.exponentialRampToValueAtTime(
                0.001,
                audio.currentTime + 0.04
            );

            oscillator.connect(gain);
            gain.connect(audio.destination);

            oscillator.start();
            oscillator.stop(audio.currentTime + 0.05);
        })();
    """)
}

actual fun pitido() {
    beepNavegador()
}