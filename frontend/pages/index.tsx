import { Link } from "@heroui/link";
import { GithubIcon } from "@/components/misc/icons";
import LandingLayout from "@/layouts/landing";
import { ArrowRightIcon } from "lucide-react";
import LinksMobile from "@/components/navbars/linksMobile";
import { useEffect } from "react";
import Lenis from "lenis";
import ScrollFrameViewer from "@/components/misc/ScrollFrameViewer";
import { HeroSection } from "@/components/landing/HeroSection";
import { FeaturesSection } from "@/components/landing/FeaturesSection";
import { ClientsSection } from "@/components/landing/ClientsSection";

export default function IndexPage() {
   useEffect(() => {
    const lenis = new Lenis({ lerp: 0.2 })

    const raf = (time: number) => {
      lenis.raf(time)
      requestAnimationFrame(raf)
    }

    requestAnimationFrame(raf)

    return () => lenis.destroy()
  }, [])

  return (
    <LandingLayout>
      <ScrollFrameViewer />
      <div className="flex lg:hidden">
        <LinksMobile />
      </div>
      <div className="relative z-10">
        <div className="px-6 container mx-auto">
          <HeroSection/>
          <FeaturesSection />
          <ClientsSection />
        </div>
      </div>
    </LandingLayout>
  );
}
