package com.example.calculadoramultiplataforma

actual fun pitido() {
    js("""
        (() => {
            const ctx = new AudioContext();
            const osc = ctx.createOscillator();
            const gain = ctx.createGain();

            osc.frequency.value = 650;
            gain.gain.setValueAtTime(0.08, ctx.currentTime);
            gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.06);

            osc.connect(gain);
            gain.connect(ctx.destination);

            osc.onended = () => ctx.close();
            osc.start();
            osc.stop(ctx.currentTime + 0.06);
        })()
    """)
}