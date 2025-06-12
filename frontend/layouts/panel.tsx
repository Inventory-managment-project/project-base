import { Head } from "./head";
import { Navbar } from "@/components/navbars/navbarPanel";

export default function PanelLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="relative flex flex-col h-screen">
      <Head />
      <Navbar />
      <main className="mx-auto w-full h-full flex-grow overflow-y-auto">
        {children}
      </main>
    </div>
  );
}
