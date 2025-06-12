import { createContext, useContext, useState } from 'react';

interface FrameContextType {
  frame: number;
  setFrame: React.Dispatch<React.SetStateAction<number>>;
}

const FrameContext = createContext<FrameContextType | undefined>(undefined);

export function FrameProvider({ children }: { children: React.ReactNode }) {
  const [frame, setFrame] = useState(0)

  return (
    <FrameContext.Provider value={{ frame, setFrame } }>
        {children}
    </FrameContext.Provider>
  )
}

export function useFrame() {
  const context = useContext(FrameContext);
  if (!context) {
    throw new Error("useFrame must be used within a FrameProvider");
  }
  return context;
}