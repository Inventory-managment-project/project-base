import type { AppProps } from "next/app";

import { HeroUIProvider } from "@heroui/system";
import { ThemeProvider as NextThemesProvider } from "next-themes";
import { useEffect, useState } from "react";
import { useRouter } from "next/router";
import { SelectedStoreProvider } from "@/context/SelectedStoreContext";
import { NotificationsProvider } from "@/context/NotificationsContext";
import { FrameProvider } from "@/context/FrameContext";

import { fontSans, fontMono } from "@/config/fonts";
import "@/styles/globals.css";

export default function App({ Component, pageProps }: AppProps) {
  const router = useRouter();
  const [forcedTheme, setForcedTheme] = useState<string | undefined>(undefined);

  useEffect(() => {
    if (router.pathname === "/") {
      setForcedTheme("light");
    } else {
      setForcedTheme(undefined);
    }

    if (typeof window !== "undefined") {
      const body = document.body;

      body.classList.remove("overflow-auto", "overflow-hidden", "overflow-visible");

      if (router.pathname.startsWith("/panel")) {
        body.classList.add("overflow-hidden");
      } else {
        body.classList.add("overflow-visible");
      }
    }
  }, [router.pathname]);

  return (
    <HeroUIProvider navigate={router.push}>
      <NextThemesProvider forcedTheme={forcedTheme} defaultTheme="light">
        <SelectedStoreProvider>
          <NotificationsProvider>
            <FrameProvider>
              <Component {...pageProps} /> 
            </FrameProvider>
          </NotificationsProvider>
        </SelectedStoreProvider>
      </NextThemesProvider>
    </HeroUIProvider>
  );
}

export const fonts = {
  sans: fontSans.style.fontFamily,
  mono: fontMono.style.fontFamily,
};
