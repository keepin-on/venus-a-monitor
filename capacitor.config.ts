import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.marstek.venusmonitor',
  appName: 'Venus A Monitor',
  webDir: 'www',
  server: {
    androidScheme: 'http',
    cleartext: true
  }
};

export default config;
