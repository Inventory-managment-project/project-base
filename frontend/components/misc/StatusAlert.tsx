import { useState } from "react";
import { Alert } from "@heroui/alert";
import { motion, AnimatePresence } from "framer-motion";

export default function StatusAlert({ show, title, description, statusCode }: { show: boolean; title: string; description: string; statusCode: number }) {
  
  return (
    <AnimatePresence>
      {show &&  (
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -20 }}
          transition={{ duration: 0.3 }}
          className="fixed bottom-4 right-4 z-50"
        >
          <Alert 
          color={statusCode.toString().startsWith("2") ? "success" : "danger"}
          title={title} 
          description={description}
          variant="faded"
          />
        </motion.div>
      )}
    </AnimatePresence>
  );
}