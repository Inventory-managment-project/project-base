import React from "react";
import PanelLayout from "@/layouts/panel";
import SideMenu from "@/components/sideMenu";

export default function PanelPage() {
  const [content, setContent] = React.useState<JSX.Element | null>(null);
  
  return (
    <PanelLayout>
      <SideMenu setContent={setContent} />
      <div className="content">{content}</div>
    </PanelLayout>
  );
}
