import { Link } from "@heroui/link";
import { Snippet } from "@heroui/snippet";
import { Code } from "@heroui/code";
import { button as buttonStyles } from "@heroui/theme";

import { siteConfig } from "@/config/site";
import { title, subtitle } from "@/components/misc/primitives";
import { GithubIcon } from "@/components/misc/icons";
import LandingLayout from "@/layouts/landing";

export default function IndexPage() {
  return (
    <LandingLayout>
      <section className="flex flex-col items-center justify-center gap-4 py-8 md:py-10">
        <div className="inline-block max-w-xl text-center justify-center">
          <span className={title()}>Esta es la página de&nbsp;</span>
          <span className={title({ color: "violet" })}>Aterrizaje&nbsp;</span>
        </div>
      </section>
    </LandingLayout>
  );
}
