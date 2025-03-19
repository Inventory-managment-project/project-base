import { Head } from "./head";
import { Navbar } from "@/components/navbarPanel";

export default function PanelLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="relative flex flex-col h-screen">
      <Head />
      <Navbar />
      <main className="mx-auto w-full h-full flex-grow">
        {children}
      </main>
    </div>
  );
}
